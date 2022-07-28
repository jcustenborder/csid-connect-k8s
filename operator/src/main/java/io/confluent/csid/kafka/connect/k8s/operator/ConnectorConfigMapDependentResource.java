package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.confluent.csid.kafka.connect.k8s.common.K8sConfig;
import io.confluent.csid.kafka.connect.k8s.common.SortedProperties;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.confluent.csid.kafka.connect.k8s.common.Utils.ifNotEmpty;

@KubernetesDependent
public class ConnectorConfigMapDependentResource extends CRUDKubernetesDependentResource<ConfigMap, KafkaConnector> {
  private static final Logger log = LoggerFactory.getLogger(ConnectorConfigMapDependentResource.class);

  public ConnectorConfigMapDependentResource() {
    super(ConfigMap.class);
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

    SortedProperties loggingProperties = new SortedProperties();
    loggingProperties.putAll(
        "log4j.rootLogger", String.format("%s, stdout", primary.getSpec().getLoggingConfig().getLevel()),
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
