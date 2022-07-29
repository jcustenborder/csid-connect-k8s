package io.confluent.csid.kafka.connect.k8s.connector;

import io.confluent.csid.kafka.connect.k8s.common.K8sConfig;
import io.confluent.csid.kafka.connect.k8s.common.SortedProperties;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.apache.kafka.connect.runtime.CloseableConnectorContext;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.ConnectorStatus;
import org.apache.kafka.connect.runtime.Herder;
import org.apache.kafka.connect.runtime.TargetState;
import org.apache.kafka.connect.runtime.Worker;
import org.apache.kafka.connect.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class K8sConnectorContext implements CloseableConnectorContext, ConnectorStatus.Listener, Callback<TargetState> {
  private static final Logger log = LoggerFactory.getLogger(K8sConnectorContext.class);
  private final Herder herder;
  private final Worker worker;
  private final K8sConfig k8sConfig;
  private final ConnectorConfig connectorConfig;
  private final DefaultKubernetesClient k8sClient;


  public K8sConnectorContext(Herder herder, Worker worker, K8sConfig k8sConfig, ConnectorConfig connectorConfig, DefaultKubernetesClient k8sClient) {
    this.herder = herder;
    this.worker = worker;
    this.k8sConfig = k8sConfig;
    this.connectorConfig = connectorConfig;
    this.k8sClient = k8sClient;
  }


  @Override
  public void requestTaskReconfiguration() {

  }

  @Override
  public void raiseError(Exception e) {

  }

  @Override
  public void onShutdown(String connectorName) {

  }

  @Override
  public void onFailure(String connectorName, Throwable throwable) {
    log.error("onFailure() - connectorName = '{}'", connectorName, throwable);
  }

  @Override
  public void onPause(String connectorName) {

  }

  @Override
  public void onResume(String connectorName) {

  }

  @Override
  public void onStartup(String connectorName) {

  }

  @Override
  public void onDeletion(String connectorName) {

  }

  @Override
  public void onRestart(String connectorName) {

  }

  @Override
  public void onCompletion(Throwable throwable, TargetState targetState) {
    if (null != throwable) {
      log.error("Exception thrown while starting connector", throwable);
      this.worker.stopAndAwaitConnector(this.k8sConfig.connectorName);
      this.worker.stop();
      this.herder.stop();
      return;
    }

    try {
      updateTaskConfigMap();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  static Map<String, String> generateTaskConfigs(List<Map<String, String>> taskConfigs) {
    Map<String, String> result = new LinkedHashMap<>();
    int index = 0;

    for (Map<String, String> taskConfig : taskConfigs) {
      String propertyFilename = String.format("task-%s.properties", index);
      SortedProperties sortedProperties = new SortedProperties(taskConfig);
      result.put(propertyFilename, sortedProperties.toString());
      index++;
    }

    return result;
  }

  void updateTaskConfigMap() throws Exception {
    log.debug("updateTaskConfigMap() - requesting task configs.");
    List<Map<String, String>> taskConfigs = this.worker.connectorTaskConfigs(this.k8sConfig.connectorName, this.connectorConfig);
    Map<String, String> configFiles = generateTaskConfigs(taskConfigs);

    ConfigMap configMap = new ConfigMapBuilder()
        .withMetadata(
            new ObjectMetaBuilder()
                .withNamespace(this.k8sConfig.connectorNamespace)
                .withName(this.k8sConfig.connectorName + "-tasks")
                .withLabels(this.k8sConfig.labels())
                .build()
        ).addToData(configFiles)
        .build();

    log.info("Creating ConfigMap {}", configMap.getMetadata());
    log.trace("Creating ConfigMap {}", configMap);
    this.k8sClient.configMaps().createOrReplace(configMap);
  }


  @Override
  public void close() {

  }
}
