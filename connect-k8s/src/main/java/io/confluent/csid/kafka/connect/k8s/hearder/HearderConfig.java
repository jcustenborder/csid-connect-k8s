package io.confluent.csid.kafka.connect.k8s.hearder;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.runtime.WorkerConfig;

import java.util.Map;

public class HearderConfig extends WorkerConfig {
  public String namespace;
  public HearderConfig(Map<String, String> props) {
    super(config(), props);
  }

  public static ConfigDef config() {
    return WorkerConfig.baseConfigDef()
        .define("namespace", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "");
  }
}
