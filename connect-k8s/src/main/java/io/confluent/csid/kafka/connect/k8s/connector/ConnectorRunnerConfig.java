package io.confluent.csid.kafka.connect.k8s.connector;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.runtime.WorkerConfig;

import java.util.Map;

public class ConnectorRunnerConfig extends WorkerConfig {
  public ConnectorRunnerConfig(Map<String, String> props) {
    super(configDef(), props);
  }

  public static ConfigDef configDef() {
    return baseConfigDef()
        .define("offset.storage.topic", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "")
        .define("offset.storage.partitions", ConfigDef.Type.INT, 10, ConfigDef.Importance.HIGH, "")
        .define("offset.storage.replication.factor", ConfigDef.Type.SHORT, (short) 3, ConfigDef.Importance.HIGH, "");
  }
}