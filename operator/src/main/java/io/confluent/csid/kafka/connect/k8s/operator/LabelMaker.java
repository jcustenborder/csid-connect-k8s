package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import java.util.Map;

class LabelMaker {

  public static final String LABEL_CONNECTOR = "io.confluent.csid.connect.connector";
  public static final String LABEL_TYPE = "io.confluent.csid.connect.type";

  public static final String VALUE_CONNECTOR = "connector";
  public static final String VALUE_TASK = "task";

  public static final String SELECTOR_TASK = LABEL_TYPE + "=" + VALUE_TASK;
  public static final String SELECTOR_CONNECTOR = LABEL_TYPE + "=" + VALUE_CONNECTOR;


  public static Map<String, String> connectorLabels(KafkaConnector connector) {
    return Map.of(
        LABEL_CONNECTOR, connector.getMetadata().getName(),
        LABEL_TYPE, VALUE_CONNECTOR
    );
  }

  public static Map<String, String> connectorLabels(ResourceID resourceID) {
    return Map.of(
        LABEL_CONNECTOR, resourceID.getName(),
        LABEL_TYPE, VALUE_CONNECTOR
    );
  }

  public static void addConnectorLabels(Map<String, String> labels, KafkaConnector connector) {
    labels.putAll(connectorLabels(connector));
  }

  public static Map<String, String> taskLabels(KafkaConnector connector) {
    return Map.of(
        LABEL_CONNECTOR, connector.getMetadata().getName(),
        LABEL_TYPE, VALUE_TASK
    );
  }
  public static Map<String, String> taskLabels(ResourceID resourceID) {
    return Map.of(
        LABEL_CONNECTOR, resourceID.getName(),
        LABEL_TYPE, VALUE_TASK
    );
  }

  public static void addTaskLabels(Map<String, String> labels, KafkaConnector connector) {
    labels.putAll(taskLabels(connector));
  }
}
