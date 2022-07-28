package io.confluent.csid.kafka.connect.k8s;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fabric8.kubernetes.api.model.LocalObjectReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KafkaConnectorSpec {
  /**
   * This is the description
   */
  private String connectorImage;
  /**
   * This is the info for the task image.
   */
  private String taskImage;

  private Map<String, String> connectorConfig = new LinkedHashMap<>();

  private List<LocalObjectReference> imagePullSecrets = new ArrayList<>();

  private LoggingConfig loggingConfig = new LoggingConfig();

  private List<String> downloadPlugins = new ArrayList<>();

  @JsonProperty(required = true)
  private LocalObjectReference workerConfigSecret;


  public LocalObjectReference getWorkerConfigSecret() {
    return workerConfigSecret;
  }

  public void setWorkerConfigSecret(LocalObjectReference workerConfigSecret) {
    this.workerConfigSecret = workerConfigSecret;
  }

  public List<String> getDownloadPlugins() {
    return downloadPlugins;
  }

  public void setDownloadPlugins(List<String> downloadPlugins) {
    this.downloadPlugins = downloadPlugins;
  }

  public LoggingConfig getLoggingConfig() {
    return loggingConfig;
  }

  public void setLoggingConfig(LoggingConfig loggingConfig) {
    this.loggingConfig = loggingConfig;
  }

  public String getImagePullPolicy() {
    return imagePullPolicy;
  }

  public void setImagePullPolicy(String imagePullPolicy) {
    this.imagePullPolicy = imagePullPolicy;
  }

  private String imagePullPolicy;


  public String getConnectorImage() {
    return connectorImage;
  }

  public void setConnectorImage(String connectorImage) {
    this.connectorImage = connectorImage;
  }

  public String getTaskImage() {
    return taskImage;
  }

  public void setTaskImage(String taskImage) {
    this.taskImage = taskImage;
  }

  public Map<String, String> getConnectorConfig() {
    return connectorConfig;
  }

  public void setConnectorConfig(Map<String, String> connectorConfig) {
    this.connectorConfig = connectorConfig;
  }

  public List<LocalObjectReference> getImagePullSecrets() {
    return imagePullSecrets;
  }

  public void setImagePullSecrets(List<LocalObjectReference> imagePullSecrets) {
    this.imagePullSecrets = imagePullSecrets;
  }


}
