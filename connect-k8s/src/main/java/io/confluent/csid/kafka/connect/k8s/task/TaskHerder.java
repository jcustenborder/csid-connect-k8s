package io.confluent.csid.kafka.connect.k8s.task;

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

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TaskHerder extends AbstractHerder {
  public TaskHerder(Worker worker, String workerId, String kafkaClusterId, StatusBackingStore statusBackingStore, ConfigBackingStore configBackingStore, ConnectorClientConfigOverridePolicy connectorClientConfigOverridePolicy) {
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

  @Override
  public void start() {

  }

  @Override
  public void stop() {

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
