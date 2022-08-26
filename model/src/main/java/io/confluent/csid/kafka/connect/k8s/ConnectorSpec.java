package io.confluent.csid.kafka.connect.k8s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.fabric8.kubernetes.api.model.LocalObjectReference;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class ConnectorSpec {

  @NotNull
  @JsonProperty("connector")
  @JsonPropertyDescription("Configuration for the connector container")
  private ContainerConfig connector;


  @NotNull
  @JsonProperty("task")
  @JsonPropertyDescription("Configuration for the task container(s).")
  private ContainerConfig task;


  @NotNull
  @JsonProperty("connectorConfig")
  @JsonPropertyDescription("The configuration for the connector")
  private Map<String, String> connectorConfig = new LinkedHashMap<>();


  private List<LocalObjectReference> imagePullSecrets = new ArrayList<>();

  @JsonProperty("loggingConfig")
  @JsonPropertyDescription("Logging configuration for the containers.")
  private LoggingConfig loggingConfig = new LoggingConfig();

  @JsonProperty("downloadPlugins")
  @JsonPropertyDescription("List of plugins to download from the Confluent hub. ex 'confluentinc/kafka-connect-datagen:0.5.3'")
  private List<String> downloadPlugins = new ArrayList<>();

  @JsonProperty("workerConfigSecret")
  @JsonPropertyDescription("Secret in the local namespace that holds the worker.properties configuration file.")
  private LocalObjectReference workerConfigSecret;

  private String imagePullPolicy;



  public ContainerConfig getConnector() {
    return connector;
  }

  public void setConnector(ContainerConfig connector) {
    this.connector = connector;
  }

  public ContainerConfig getTask() {
    return task;
  }

  public void setTask(ContainerConfig task) {
    this.task = task;
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

  public LoggingConfig getLoggingConfig() {
    return loggingConfig;
  }

  public void setLoggingConfig(LoggingConfig loggingConfig) {
    this.loggingConfig = loggingConfig;
  }

  public List<String> getDownloadPlugins() {
    return downloadPlugins;
  }

  public void setDownloadPlugins(List<String> downloadPlugins) {
    this.downloadPlugins = downloadPlugins;
  }

  public LocalObjectReference getWorkerConfigSecret() {
    return workerConfigSecret;
  }

  public void setWorkerConfigSecret(LocalObjectReference workerConfigSecret) {
    this.workerConfigSecret = workerConfigSecret;
  }

  public String getImagePullPolicy() {
    return imagePullPolicy;
  }

  public void setImagePullPolicy(String imagePullPolicy) {
    this.imagePullPolicy = imagePullPolicy;
  }
}
