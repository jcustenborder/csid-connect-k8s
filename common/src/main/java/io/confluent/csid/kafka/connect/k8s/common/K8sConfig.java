package io.confluent.csid.kafka.connect.k8s.common;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class K8sConfig extends AbstractConfig {

  public static final String CONNECTOR_NAMESPACE_CONF = "connector.namespace";
  public static final String CONNECTOR_NAME_CONF = "connector.name";
  public static final String PLUGIN_DOWNLOAD_CONF = "plugin.download";

  public final String connectorName;
  public final String connectorNamespace;
  public final List<String> pluginDownload;

  public String connectGroupId() {
    return String.format("%s-%s", this.connectorNamespace, this.connectorName);
  }
  public K8sConfig(Map<?, ?> originals) {
    super(configDef(), originals);
    this.connectorName = getString(CONNECTOR_NAME_CONF);
    this.connectorNamespace = getString(CONNECTOR_NAMESPACE_CONF);
    this.pluginDownload = getList(PLUGIN_DOWNLOAD_CONF);
  }

  static ConfigDef configDef() {
    return new ConfigDef()
        .define(CONNECTOR_NAMESPACE_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "")
        .define(CONNECTOR_NAME_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "")
        .define(PLUGIN_DOWNLOAD_CONF, ConfigDef.Type.LIST, new ArrayList<>(), ConfigDef.Importance.HIGH, "");
  }
}
