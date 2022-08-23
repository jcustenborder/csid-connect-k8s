package io.confluent.csid.kafka.connect.k8s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoggingConfig {
  @JsonProperty("defaultLoggerLevel")
  @JsonPropertyDescription("The default logger level for the logging.properties. Value values are TRACE, DEBUG, INFO, WARN, ERROR")
  String defaultLoggerLevel = "INFO";

  @NotNull
  @JsonProperty("loggerLevels")
  @JsonPropertyDescription("Individual logger overrides that will be written to the logging.properties. The keys are the loggers and the values are the level. For example io.confluent.csid.kafka.connect.k8s.common=TRACE")
  Map<String, String> loggerLevels = new LinkedHashMap<>();

  public String getDefaultLoggerLevel() {
    return defaultLoggerLevel;
  }

  public void setDefaultLoggerLevel(String defaultLoggerLevel) {
    this.defaultLoggerLevel = defaultLoggerLevel;
  }

  public Map<String, String> getLoggerLevels() {
    return loggerLevels;
  }

  public void setLoggerLevels(Map<String, String> loggerLevels) {
    this.loggerLevels = loggerLevels;
  }
}
