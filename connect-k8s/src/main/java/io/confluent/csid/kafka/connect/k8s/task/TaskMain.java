package io.confluent.csid.kafka.connect.k8s.task;

import io.confluent.csid.kafka.connect.k8s.common.ConfigHelper;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.connect.connector.policy.ConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.connector.policy.NoneConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.runtime.Herder;
import org.apache.kafka.connect.runtime.TargetState;
import org.apache.kafka.connect.runtime.TaskStatus;
import org.apache.kafka.connect.runtime.Worker;
import org.apache.kafka.connect.runtime.distributed.ClusterConfigState;
import org.apache.kafka.connect.runtime.isolation.Plugins;
import org.apache.kafka.connect.runtime.standalone.StandaloneHerder;
import org.apache.kafka.connect.storage.KafkaOffsetBackingStore;
import org.apache.kafka.connect.storage.MemoryConfigBackingStore;
import org.apache.kafka.connect.storage.MemoryStatusBackingStore;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.apache.kafka.connect.util.ConnectorTaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TaskMain {
  private static final Logger log = LoggerFactory.getLogger(TaskMain.class);

  public static void main(String[] args) throws Exception {
    ConfigHelper configHelper = ConfigHelper.create(args);
    int taskIndex = configHelper.taskIndex.orElseThrow(() -> new IllegalStateException("Could not determine the task index based on the hostname."));
    log.info("TaskIndex = {}", taskIndex);

    Map<String, String> workerSettings = configHelper.workerSettings();
    Map<String, String> taskSettings = configHelper.taskSettings();
    Map<String, String> connectorSettings = configHelper.connectorSettings();

    TaskRunnerConfig config = new TaskRunnerConfig(workerSettings);
    Plugins plugins = new Plugins(workerSettings);
    plugins.compareAndSwapWithDelegatingLoader();
    String workerId = configHelper.hostname;
    OffsetBackingStore offsetBackingStore = new KafkaOffsetBackingStore();
    ConnectorClientConfigOverridePolicy overridePolicy = new NoneConnectorClientConfigOverridePolicy();
    final Worker worker = new Worker(workerId, Time.SYSTEM, plugins, config, offsetBackingStore, overridePolicy);
    worker.start();
    String kafkaClusterId = "";

//    final Herder herder = new TaskHerder(worker, workerId, kafkaClusterId, new MemoryStatusBackingStore(), new MemoryConfigBackingStore(), overridePolicy);
    final Herder herder = new StandaloneHerder(worker, kafkaClusterId, overridePolicy);
    herder.start();


    ConnectorTaskId connectorTaskId = configHelper.taskId();

    TaskStatus.Listener statusListener = new TaskStatus.Listener() {
      @Override
      public void onStartup(ConnectorTaskId connectorTaskId) {
        log.info("onStartup - {}", connectorTaskId);
      }

      @Override
      public void onPause(ConnectorTaskId connectorTaskId) {
        log.info("onPause - {}", connectorTaskId);
      }

      @Override
      public void onResume(ConnectorTaskId connectorTaskId) {
        log.info("onResume - {}", connectorTaskId);

      }

      @Override
      public void onFailure(ConnectorTaskId connectorTaskId, Throwable throwable) {
        log.error("onFailure - {}", connectorTaskId, throwable);
        worker.stop();
        herder.stop();
      }

      @Override
      public void onShutdown(ConnectorTaskId connectorTaskId) {
        log.info("onShutdown - {}", connectorTaskId);
      }

      @Override
      public void onDeletion(ConnectorTaskId connectorTaskId) {
        log.info("onDeletion - {}", connectorTaskId);
      }

      @Override
      public void onRestart(ConnectorTaskId connectorTaskId) {
        log.info("onRestart - {}", connectorTaskId);
      }
    };

    worker.startTask(connectorTaskId, ClusterConfigState.EMPTY, connectorSettings, taskSettings, statusListener, TargetState.STARTED);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shutdown signal received. Shutting down {}", connectorTaskId);
      worker.stopAndAwaitTask(connectorTaskId);
      worker.stop();
      herder.stop();
    }));

  }
}
