package io.confluent.csid.kafka.connect.k8s.task;

import io.confluent.csid.kafka.connect.k8s.RunnerWorkerConfig;
import io.confluent.csid.kafka.connect.k8s.common.K8sConfig;
import io.confluent.csid.kafka.connect.k8s.common.PluginDownloader;
import io.confluent.csid.kafka.connect.k8s.connector.ConnectorRunnerOptions;
import io.confluent.csid.kafka.connect.k8s.connector.K8sConnectorContext;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.connect.connector.policy.ConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.connector.policy.NoneConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.Herder;
import org.apache.kafka.connect.runtime.TargetState;
import org.apache.kafka.connect.runtime.TaskStatus;
import org.apache.kafka.connect.runtime.Worker;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.runtime.distributed.ClusterConfigState;
import org.apache.kafka.connect.runtime.isolation.Plugins;
import org.apache.kafka.connect.runtime.rest.InternalRequestSignature;
import org.apache.kafka.connect.runtime.standalone.StandaloneHerder;
import org.apache.kafka.connect.storage.KafkaOffsetBackingStore;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.apache.kafka.connect.util.ConnectorTaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TaskRunner {
  private static final Logger log = LoggerFactory.getLogger(TaskRunner.class);

  public static void main(String[] args) throws Exception {
    TaskRunnerOptions options = TaskRunnerOptions.create(args);

    Map<String, String> workerSettings = options.workerSettings();

    Map<String, String> connectorSettings = options.connectorSettings();
    Map<String, String> k8sSettings = options.k8sSettings();
    Map<String, String> taskSettings = options.taskSettings();
    K8sConfig k8sConfig = new K8sConfig(k8sSettings);

    workerSettings.put(WorkerConfig.CONNECT_GROUP_ID, k8sConfig.connectGroupId());

    PluginDownloader pluginDownloader = new PluginDownloader(k8sConfig);
    pluginDownloader.download();

    RunnerWorkerConfig config = new RunnerWorkerConfig(workerSettings);
    Plugins plugins = new Plugins(workerSettings);
    plugins.compareAndSwapWithDelegatingLoader();

    OffsetBackingStore offsetBackingStore = new KafkaOffsetBackingStore();

    //TODO: Comeback and resolve this. We should not be defaulting to none
    ConnectorClientConfigOverridePolicy overridePolicy = new NoneConnectorClientConfigOverridePolicy();
    final Worker worker = new Worker(k8sConfig.connectorName, Time.SYSTEM, plugins, config, offsetBackingStore, overridePolicy);
    worker.start();
    String kafkaClusterId = "";
//    ConnectorConfig connectorConfig = new ConnectorConfig(plugins, connectorSettings);

    final Herder herder = new StandaloneHerder(worker, kafkaClusterId, overridePolicy);
    herder.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shutdown hook received.");
      worker.stopAndAwaitConnector(k8sConfig.connectorName);
      worker.stop();
      herder.stop();
    }));

//    K8sConnectorContext context = new K8sConnectorContext(herder, worker, k8sConfig, connectorConfig, client);
    ConnectorTaskId connectorTaskId = new ConnectorTaskId(k8sConfig.connectorName, options.taskIndex);
    TaskStatusListener taskStatusListener = new TaskStatusListener();
    worker.startTask(connectorTaskId, ClusterConfigState.EMPTY, connectorSettings, taskSettings, taskStatusListener, TargetState.STARTED);
  }
}
