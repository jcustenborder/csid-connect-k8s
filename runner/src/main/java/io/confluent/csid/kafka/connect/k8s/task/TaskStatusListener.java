package io.confluent.csid.kafka.connect.k8s.task;

import org.apache.kafka.connect.runtime.TaskStatus;
import org.apache.kafka.connect.util.ConnectorTaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskStatusListener implements  TaskStatus.Listener{
  private static final Logger log = LoggerFactory.getLogger(TaskStatusListener.class);
  @Override
  public void onStartup(ConnectorTaskId connectorTaskId) {
    log.info("{} started", connectorTaskId);
  }

  @Override
  public void onPause(ConnectorTaskId connectorTaskId) {

  }

  @Override
  public void onResume(ConnectorTaskId connectorTaskId) {

  }

  @Override
  public void onFailure(ConnectorTaskId connectorTaskId, Throwable throwable) {
    log.error("{} failed", connectorTaskId, throwable);
  }

  @Override
  public void onShutdown(ConnectorTaskId connectorTaskId) {

  }

  @Override
  public void onDeletion(ConnectorTaskId connectorTaskId) {

  }

  @Override
  public void onRestart(ConnectorTaskId connectorTaskId) {

  }
}
