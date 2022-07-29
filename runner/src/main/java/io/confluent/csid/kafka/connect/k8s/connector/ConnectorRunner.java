package io.confluent.csid.kafka.connect.k8s.connector;

import io.confluent.csid.kafka.connect.k8s.RunnerWorkerConfig;
import io.confluent.csid.kafka.connect.k8s.common.K8sConfig;
import io.confluent.csid.kafka.connect.k8s.common.PluginDownloader;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.connect.connector.policy.ConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.connector.policy.NoneConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.Herder;
import org.apache.kafka.connect.runtime.TargetState;
import org.apache.kafka.connect.runtime.Worker;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.runtime.isolation.Plugins;
import org.apache.kafka.connect.runtime.standalone.StandaloneHerder;
import org.apache.kafka.connect.storage.KafkaOffsetBackingStore;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ConnectorRunner {
  private static final Logger log = LoggerFactory.getLogger(ConnectorRunner.class);

  public static void main(String[] args) throws Exception {
    ConnectorRunnerOptions options = ConnectorRunnerOptions.create(args);

    Map<String, String> workerSettings = options.workerSettings();

    Map<String, String> connectorSettings = options.connectorSettings();
    Map<String, String> k8sSettings = options.k8sSettings();
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


    final Herder herder = new StandaloneHerder(worker, kafkaClusterId, overridePolicy);
    herder.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shutdown hook received.");
      worker.stopAndAwaitConnector(k8sConfig.connectorName);
      worker.stop();
      herder.stop();
    }));

    ConnectorConfig connectorConfig = new ConnectorConfig(plugins, connectorSettings);

    DefaultKubernetesClient client = new DefaultKubernetesClient();

    K8sConnectorContext context = new K8sConnectorContext(herder, worker, k8sConfig, connectorConfig, client);

    worker.startConnector(k8sConfig.connectorName, connectorSettings, context, context, TargetState.STARTED, context);


  }
}
