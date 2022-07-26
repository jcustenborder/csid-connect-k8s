package io.confluent.csid.kafka.connect.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ConfigMapVolumeSource;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1LabelSelector;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import io.kubernetes.client.openapi.models.V1StatefulSetSpec;
import io.kubernetes.client.openapi.models.V1StatefulSetUpdateStrategy;
import io.kubernetes.client.openapi.models.V1Volume;
import io.kubernetes.client.openapi.models.V1VolumeDevice;
import io.kubernetes.client.openapi.models.V1VolumeMount;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class);


  static String toString(Properties properties) {
    String configBody;
    try (StringWriter writer = new StringWriter()) {
      properties.store(writer, null);
      configBody = writer.toString();
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
    String[] parts = configBody.split("\\r?\\n");
    return Stream.of(parts).filter(s -> !s.startsWith("#")).collect(Collectors.joining("\n"));
  }

  static void createConfigMap(K8sClient k8sClient, String namespace, V1ConfigMap configMap) throws ApiException {
    try {
      k8sClient.coreV1Api().replaceNamespacedConfigMap(
          configMap.getMetadata().getName(),
          namespace,
          configMap,
          null,
          null,
          null,
          null
      );
    } catch (ApiException ex) {
      if (404 == ex.getCode()) {
        k8sClient.coreV1Api().createNamespacedConfigMap(
            namespace,
            configMap,
            null,
            null,
            null,
            null
        );
      } else {
        throw ex;
      }
    }
  }

  static void createStatefulSet(K8sClient k8sClient, V1StatefulSet connectorStatefulSet, String namespace) throws ApiException {

    log.info("\n{}", Yaml.dump(connectorStatefulSet));


    try {
      k8sClient.appsV1Api().replaceNamespacedStatefulSet(connectorStatefulSet.getMetadata().getName(), namespace, connectorStatefulSet, null, null, null, null);
    } catch (ApiException ex) {
      log.warn("Exception thrown:\n{}", ex, ex.getResponseBody());
      if (404 == ex.getCode()) {
        k8sClient.appsV1Api().createNamespacedStatefulSet(namespace, connectorStatefulSet, null, null, null, null);
      } else {
        throw ex;
      }
    }


  }

  public static void main(String[] args) throws Exception {
    ApiClient client = Config.defaultClient();

    K8sClient k8sClient = ImmutableK8sClient.builder()
        .client(client)
        .build();

    ConnectorMetadata metadata = ImmutableConnectorMetadata.builder()
        .connectorName("test-jdbc-source")
        .namespace("kafka-connect-poc")
        .cluster("asdf")
        .taskCount(10)
        .build();

    String namespace = "kafka-connect-poc";



    V1ConfigMap taskConfigMap = new V1ConfigMap();
    taskConfigMap.metadata(metadata.configMetaTask());
    taskConfigMap.putDataItem("worker.properties", "#foo");

    for (int i = 1; i <= metadata.taskCount(); i++) {
      Properties properties = new Properties();
      properties.put("task.class", "asdfasdfasdfsdasdfassdf");
      properties.put("task.index", Integer.toString(i));
      taskConfigMap.putDataItem(
          String.format("task%s.properties", i),
          toString(properties)
      );
    }

    log.info("{}", taskConfigMap);

    createConfigMap(k8sClient, namespace, taskConfigMap);

    Properties connectorProperties = new Properties();
    connectorProperties.put("connector.class", "asdfeasdfasdf");

    V1ConfigMap connectorConfigMap = new V1ConfigMap();
    connectorConfigMap.metadata(metadata.configMetaConnector());
    connectorConfigMap.putDataItem("connector.properties", toString(connectorProperties));
    createConfigMap(k8sClient, namespace, connectorConfigMap);

    try {
      createStatefulSet(k8sClient, metadata.connectorStatefulSet(), metadata.namespace());
      createStatefulSet(k8sClient, metadata.taskStatefulSet(), metadata.namespace());
    } catch (ApiException ex) {
      log.error("Exception thrown: Response body:\n{}", ex.getResponseBody(), ex);
    }


  }
}