package io.confluent.csid.kafka.connect.k8s.connector;

import io.confluent.csid.kafka.connect.k8s.ImmutableK8sClient;
import io.confluent.csid.kafka.connect.k8s.K8sClient;
import io.confluent.csid.kafka.connect.k8s.common.ConfigHelper;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.connect.connector.policy.ConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.connector.policy.NoneConnectorClientConfigOverridePolicy;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.Herder;
import org.apache.kafka.connect.runtime.TargetState;
import org.apache.kafka.connect.runtime.Worker;
import org.apache.kafka.connect.runtime.isolation.Plugins;
import org.apache.kafka.connect.runtime.standalone.StandaloneHerder;
import org.apache.kafka.connect.storage.KafkaOffsetBackingStore;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ConnectorMain {
  private static final Logger log = LoggerFactory.getLogger(ConnectorMain.class);

  public static void main(String[] args) throws Exception {
    ConfigHelper configHelper = ConfigHelper.create(args);
    Map<String, String> workerSettings = configHelper.workerSettings();
    Map<String, String> connectorSettings = configHelper.connectorSettings();




    ConnectorRunnerConfig config = new ConnectorRunnerConfig(workerSettings);
    Plugins plugins = new Plugins(workerSettings);
    plugins.compareAndSwapWithDelegatingLoader();
    String workerId = configHelper.hostname;
    OffsetBackingStore offsetBackingStore = new KafkaOffsetBackingStore();

    ConnectorClientConfigOverridePolicy overridePolicy = new NoneConnectorClientConfigOverridePolicy();
    final Worker worker = new Worker(workerId, Time.SYSTEM, plugins, config, offsetBackingStore, overridePolicy);
    worker.start();
    String kafkaClusterId = "";
    ConnectorConfig connectorConfig = new ConnectorConfig(plugins, connectorSettings);

    final Herder herder = new StandaloneHerder(worker, kafkaClusterId, overridePolicy);
    herder.start();

    ApiClient client = Config.defaultClient();

    K8sClient k8sClient = ImmutableK8sClient.builder()
        .client(client)
        .build();
    K8sConnectorContext context = new K8sConnectorContext(herder, worker, configHelper, connectorConfig, k8sClient);

    worker.startConnector(configHelper.connectorName, connectorSettings, context, context, TargetState.STARTED, context);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shutdown hook received.");
      worker.stopAndAwaitConnector(configHelper.connectorName);
      worker.stop();
      herder.stop();
    }));
  }
}
