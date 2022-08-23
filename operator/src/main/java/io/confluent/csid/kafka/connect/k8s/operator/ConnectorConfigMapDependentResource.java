package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.confluent.csid.kafka.connect.k8s.common.K8sConfig;
import io.confluent.csid.kafka.connect.k8s.common.SortedProperties;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.confluent.csid.kafka.connect.k8s.common.Utils.ifNotEmpty;
import static io.confluent.csid.kafka.connect.k8s.operator.LabelMaker.connectorLabels;
import static io.confluent.csid.kafka.connect.k8s.operator.LabelMaker.taskLabels;

@KubernetesDependent(labelSelector = LabelMaker.SELECTOR_CONNECTOR)
public class ConnectorConfigMapDependentResource extends CRUDKubernetesDependentResource<ConfigMap, KafkaConnector> {
  private static final Logger log = LoggerFactory.getLogger(ConnectorConfigMapDependentResource.class);

  public ConnectorConfigMapDependentResource() {
    super(ConfigMap.class);
  }

  @Override
  protected void onUpdated(ResourceID primaryResourceId, ConfigMap updated, ConfigMap actual) {
    super.onUpdated(primaryResourceId, updated, actual);

    Map<String, String> labels = connectorLabels(primaryResourceId);

    log.debug("onUpdated({}) - Searching for pods with labels: {}", primaryResourceId, labels);
    if (primaryResourceId.getNamespace().isPresent()) {
      PodList podList =
          getKubernetesClient().pods().inNamespace(primaryResourceId.getNamespace().get())
              .withLabels(labels)
              .list();
      for (Pod pod : podList.getItems()) {
        log.info("onUpdated({}) - Restarting pod {}", primaryResourceId, pod.getMetadata().getName());
        getKubernetesClient().pods().inNamespace(primaryResourceId.getNamespace().get()).delete(pod);
      }
    } else {
      PodList podList =
          getKubernetesClient().pods()
              .withLabels(labels)
              .list();
      for (Pod pod : podList.getItems()) {
        log.info("onUpdated({}) - Restarting pod {}", primaryResourceId, pod.getMetadata().getName());
        getKubernetesClient().pods().delete(pod);
      }
    }
  }

  @Override
  protected ConfigMap desired(KafkaConnector primary, Context<KafkaConnector> context) {
    KafkaConnectorState state = ImmutableKafkaConnectorState.builder()
        .kafkaConnector(primary)
        .build();

    SortedProperties k8sProperties = new SortedProperties();
    k8sProperties.put(K8sConfig.CONNECTOR_NAME_CONF, state.name());
    k8sProperties.put(K8sConfig.CONNECTOR_NAMESPACE_CONF, state.namespace());
    ifNotEmpty(primary.getSpec().getDownloadPlugins(), p -> k8sProperties.put(K8sConfig.PLUGIN_DOWNLOAD_CONF, String.join(",", p)));

    taskLabels(primary).forEach((key, value) -> k8sProperties.put("labels." + key, value));

    SortedProperties loggingProperties = new SortedProperties();
    loggingProperties.putAll(
        "log4j.rootLogger", String.format("%s, stdout", primary.getSpec().getLoggingConfig().getDefaultLoggerLevel()),
        "log4j.appender.stdout", "org.apache.log4j.ConsoleAppender",
        "log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout",
        "log4j.appender.stdout.layout.ConversionPattern", "[%d] %p %m (%c)%n",
        "log4j.logger.org.reflections", "ERROR"
    );

    primary.getSpec().getLoggingConfig().getLoggerLevels().forEach((key, value) -> loggingProperties.put(String.format("log4j.logger.%s", key), value));

    SortedProperties connectorProperties = new SortedProperties(primary.getSpec().getConnectorConfig());
    connectorProperties.put("name", state.name());
    return new ConfigMapBuilder()
        .withMetadata(state.connectorConfigMap().build())
        .addToData("connector.properties", connectorProperties.toString())
        .addToData("logging.properties", loggingProperties.toString())
        .addToData("k8s.properties", k8sProperties.toString())
        .build();
  }
}
