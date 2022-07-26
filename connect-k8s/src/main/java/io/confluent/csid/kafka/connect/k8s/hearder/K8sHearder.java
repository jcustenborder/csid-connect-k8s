package io.confluent.csid.kafka.connect.k8s.hearder;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Watch;
import org.apache.kafka.connect.connector.policy.ConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.runtime.AbstractHerder;
import org.apache.kafka.connect.runtime.HerderRequest;
import org.apache.kafka.connect.runtime.RestartRequest;
import org.apache.kafka.connect.runtime.Worker;
import org.apache.kafka.connect.runtime.rest.InternalRequestSignature;
import org.apache.kafka.connect.runtime.rest.entities.ConnectorInfo;
import org.apache.kafka.connect.runtime.rest.entities.ConnectorStateInfo;
import org.apache.kafka.connect.runtime.rest.entities.TaskInfo;
import org.apache.kafka.connect.storage.ConfigBackingStore;
import org.apache.kafka.connect.storage.StatusBackingStore;
import org.apache.kafka.connect.util.Callback;
import org.apache.kafka.connect.util.ConnectorTaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class K8sHearder extends AbstractHerder {
  private static final Logger log = LoggerFactory.getLogger(K8sHearder.class);

  private ApiClient apiClient;
  private CoreV1Api coreV1Api;
  private Watch<V1Pod> podWatch;

  private HearderConfig config;


  public K8sHearder(Worker worker, String workerId, String kafkaClusterId, StatusBackingStore statusBackingStore, ConfigBackingStore configBackingStore, ConnectorClientConfigOverridePolicy connectorClientConfigOverridePolicy) {
    super(worker, workerId, kafkaClusterId, statusBackingStore, configBackingStore, connectorClientConfigOverridePolicy);

  }

  @Override
  protected int generation() {
    return 0;
  }

  @Override
  protected Map<String, String> rawConfig(String s) {
    return null;
  }

  Thread watchThread;

  @Override
  public void start() {
    log.info("Loading Initial State from Kubernetes");
    V1PodList initialState;
    try {
      initialState = this.coreV1Api.listNamespacedPod(
          this.config.namespace,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null
      );
    } catch (ApiException ex) {
      throw new IllegalStateException("Exception thrown loading initial pod state from Kubernetes", ex);
    }

    try {
      this.podWatch = Watch.createWatch(
          this.apiClient,
          this.coreV1Api.listNamespacedPodCall(this.config.namespace,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null
          ),
          new TypeToken<Watch.Response<V1Pod>>() {
          }.getType()
      );
    } catch (ApiException e) {
      throw new IllegalStateException("Exception thrown setting up watch in Kubernetes", e);
    }

    Runnable runnable = () -> {
      for (Watch.Response<V1Pod> event : podWatch) {
        log.debug(
            "name = '{}' type = '{}'",
            event.object.getMetadata().getName(),
            event.type
        );
      }
    };

    this.watchThread = new Thread(runnable);
    this.watchThread.setDaemon(true);
    this.watchThread.start();
  }

  @Override
  public void stop() {
    this.watchThread.interrupt();
    try {
      this.watchThread.join(1000);
    } catch (InterruptedException e) {
      log.warn("", e);
    }
  }

  @Override
  public void connectors(Callback<Collection<String>> callback) {

  }

  @Override
  public void connectorInfo(String s, Callback<ConnectorInfo> callback) {

  }

  @Override
  public void tasksConfig(String s, Callback<Map<ConnectorTaskId, Map<String, String>>> callback) {

  }

  @Override
  public void putConnectorConfig(String s, Map<String, String> map, boolean b, Callback<Created<ConnectorInfo>> callback) {

  }

  @Override
  public void deleteConnectorConfig(String s, Callback<Created<ConnectorInfo>> callback) {

  }

  @Override
  public void requestTaskReconfiguration(String s) {

  }

  @Override
  public void taskConfigs(String s, Callback<List<TaskInfo>> callback) {

  }

  @Override
  public void putTaskConfigs(String s, List<Map<String, String>> list, Callback<Void> callback, InternalRequestSignature internalRequestSignature) {

  }

  @Override
  public void restartTask(ConnectorTaskId connectorTaskId, Callback<Void> callback) {

  }

  @Override
  public void restartConnector(String s, Callback<Void> callback) {

  }

  @Override
  public HerderRequest restartConnector(long l, String s, Callback<Void> callback) {
    return null;
  }

  @Override
  public void restartConnectorAndTasks(RestartRequest restartRequest, Callback<ConnectorStateInfo> callback) {

  }
}
