package io.confluent.csid.kafka.connect.k8s;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoggingConfig {
  String level = "INFO";
  Map<String, String> loggerLevels = new LinkedHashMap<>();

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public Map<String, String> getLoggerLevels() {
    return loggerLevels;
  }

  public void setLoggerLevels(Map<String, String> loggerLevels) {
    this.loggerLevels = loggerLevels;
  }
}
