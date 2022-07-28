package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.ConfigMapVolumeSourceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.SecretVolumeSource;
import io.fabric8.kubernetes.api.model.SecretVolumeSourceBuilder;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeBuilder;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;
import org.immutables.value.Value;

import java.util.LinkedHashMap;
import java.util.Map;

@Value.Immutable
public interface KafkaConnectorState {

  KafkaConnector kafkaConnector();

  @Value.Derived
  default String name() {
    return kafkaConnector().getMetadata().getName();
  }

  @Value.Derived
  default String namespace() {
    return kafkaConnector().getMetadata().getNamespace();
  }

  @Value.Derived
  default Map<String, String> connectorLabels() {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("connector", name());
    labels.put("type", "connector");
    return labels;
  }

  @Value.Derived
  default Map<String, String> taskLabels() {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("connector", name());
    labels.put("type", "task");
    return labels;
  }


//  @Value.Derived
//  default Map<String, String> connectorMatchLabels() {
//    Map<String, String> labels = new LinkedHashMap<>();
//    labels.put("connector", name());
//    return labels;
//  }

  @Value.Derived
  default String connectorConfigMapName() {
    return String.format("%s-connector", name());
  }

  @Value.Derived
  default ObjectMetaBuilder connectorConfigMap() {
    return new ObjectMetaBuilder()
        .withName(connectorConfigMapName())
        .withLabels(connectorLabels())
        .withNamespace(namespace());
  }

  @Value.Derived
  default Volume connectorConfigMapVolume() {
    return new VolumeBuilder()
        .withName("connector-config")
        .withConfigMap(
            new ConfigMapVolumeSourceBuilder()
                .withName(connectorConfigMapName())
                .build()
        )
        .build();
  }

  @Value.Derived
  default VolumeMount connectorConfigMapVolumeMount() {
    return new VolumeMountBuilder()
        .withName("connector-config")
        .withMountPath("/config/connector")
        .withReadOnly(true)
        .build();
  }


  @Value.Derived
  default Volume workerConfigMapVolume() {
    return new VolumeBuilder()
        .withName("worker-config")
        .withSecret(
            new SecretVolumeSourceBuilder()
                .withSecretName(kafkaConnector().getSpec().getWorkerConfigSecret().getName())
                .withOptional(false)
                .build()
        )
        .build();
  }

  @Value.Derived
  default VolumeMount workerConfigMapVolumeMount() {
    return new VolumeMountBuilder()
        .withName("worker-config")
        .withMountPath("/config/worker")
        .withReadOnly(true)
        .build();
  }

  @Value.Derived
  default ObjectMetaBuilder connectorStatefulSet() {
    return connectorConfigMap();
  }


  @Value.Derived
  default String taskConfigMapName() {
    return String.format("%s-tasks", name());
  }

  @Value.Derived
  default ObjectMetaBuilder taskConfigMap() {
    return new ObjectMetaBuilder()
        .withName(taskConfigMapName())
        .withLabels(taskLabels())
        .withNamespace(namespace());
  }

  @Value.Derived
  default Volume taskConfigMapVolume() {
    return new VolumeBuilder()
        .withName("task-config")
        .withConfigMap(
            new ConfigMapVolumeSourceBuilder()
                .withName(taskConfigMapName())
                .build()
        )
        .build();
  }

  @Value.Derived
  default VolumeMount taskConfigMapVolumeMount() {
    return new VolumeMountBuilder()
        .withName("task-config")
        .withMountPath("/config/task")
        .build();
  }
  @Value.Derived
  default ObjectMetaBuilder taskStatefulSet() {
    return taskConfigMap();
  }
}
