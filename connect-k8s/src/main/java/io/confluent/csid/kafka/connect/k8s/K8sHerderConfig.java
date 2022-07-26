package io.confluent.csid.kafka.connect.k8s;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class K8sHerderConfig extends AbstractConfig {

  public final String taskImage;
  public final String connectorImage;

  public K8sHerderConfig(Map<?, ?> originals) {
    super(config(), originals);
    this.taskImage = getString("task.image");
    this.connectorImage = getString("connector.image");
  }

  static ConfigDef config() {
    return new ConfigDef()
        .define("connector.image", ConfigDef.Type.STRING, "ubuntu:latest", ConfigDef.Importance.HIGH, "")
        .define("task.image", ConfigDef.Type.STRING, "ubuntu:latest", ConfigDef.Importance.HIGH, "");
  }
}
