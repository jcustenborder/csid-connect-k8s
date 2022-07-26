package io.confluent.csid.kafka.connect.k8s.connector;

import io.confluent.csid.kafka.connect.k8s.K8sClient;
import io.confluent.csid.kafka.connect.k8s.common.ConfigHelper;
import io.confluent.csid.kafka.connect.k8s.common.SortedProperties;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.Yaml;
import org.apache.kafka.connect.runtime.CloseableConnectorContext;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.ConnectorStatus;
import org.apache.kafka.connect.runtime.Herder;
import org.apache.kafka.connect.runtime.TargetState;
import org.apache.kafka.connect.runtime.Worker;
import org.apache.kafka.connect.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class K8sConnectorContext implements CloseableConnectorContext, ConnectorStatus.Listener, Callback<TargetState> {
  private static final Logger log = LoggerFactory.getLogger(K8sConnectorContext.class);
  private final Herder herder;
  private final Worker worker;
  private final ConfigHelper configHelper;
  private final ConnectorConfig connectorConfig;
  private final K8sClient k8sClient;


  public K8sConnectorContext(Herder herder, Worker worker, ConfigHelper configHelper, ConnectorConfig connectorConfig, K8sClient k8sClient) {
    this.herder = herder;
    this.worker = worker;
    this.configHelper = configHelper;
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
      this.worker.stopAndAwaitConnector(this.configHelper.connectorName);
      this.worker.stop();
      this.herder.stop();
      return;
    }

    try {
      updateTaskConfigMap();
    } catch (ApiException e) {
      throw new RuntimeException(e);
    }
  }


  static Map<String, String> generateTaskConfigs(List<Map<String, String>> taskConfigs) {
    Map<String, String> result = new LinkedHashMap<>();
    int index = 1;

    for (Map<String, String> taskConfig : taskConfigs) {
      String propertyFilename = String.format("task-%s.properties", index);
      SortedProperties sortedProperties = new SortedProperties(taskConfig);
      result.put(propertyFilename, sortedProperties.toString());
      index++;
    }

    return result;
  }


  enum Mode {
    Update,
    Create
  }

  void updateTaskConfigMap() throws ApiException {
    log.debug("updateTaskConfigMap() - requesting task configs.");
    List<Map<String, String>> taskConfigs = this.worker.connectorTaskConfigs(this.configHelper.connectorName, this.connectorConfig);
    Map<String, String> configFiles = generateTaskConfigs(taskConfigs);

    String namespace = "kafka-connect-poc";
    String configMapName = String.format("%s-task", this.configHelper.connectorName);

    try {
      configFiles.put("worker.properties", new SortedProperties(this.configHelper.workerSettings()).toString());
      configFiles.put("connector.properties", new SortedProperties(this.configHelper.connectorSettings()).toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    V1ConfigMap configMap;
    Mode mode;
    try {
      configMap = this.k8sClient.coreV1Api().readNamespacedConfigMap(configMapName, namespace, null);
      mode = Mode.Update;
    } catch (ApiException e) {
      if (404 == e.getCode()) {
        configMap = new V1ConfigMap()
            .metadata(new V1ObjectMeta()
                .name(configMapName)
                .namespace(namespace)
            );
        mode = Mode.Create;
      } else {
        throw new IllegalStateException(e);
      }
    }

    boolean update;
    if (!configMap.getData().equals(configFiles)) {
      configMap.setData(configFiles);
      update = true;
    } else {
      update = false;
    }

    if(update && mode == Mode.Create) {
      this.k8sClient.coreV1Api().createNamespacedConfigMap(
          namespace,
          configMap,
          null,
          null,
          null,
          null
      );
    } else if (update && mode == Mode.Update) {
      this.k8sClient.coreV1Api().replaceNamespacedConfigMap(
          configMapName,
          namespace,
          configMap,
          null,
          null,
          null,
          null
      );
    } else {

    }

    V1ConfigMap taskConfigMap = new V1ConfigMap();
    taskConfigMap.metadata(new V1ObjectMeta().name(this.configHelper.connectorName));
    taskConfigMap.setData(configFiles);

    if (log.isTraceEnabled()) {
      log.trace("\n{}", Yaml.dump(taskConfigMap));
    }
  }


  @Override
  public void close() {

  }
}
