package io.confluent.csid.kafka.connect.k8s;

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
import io.kubernetes.client.openapi.models.V1VolumeMount;
import org.immutables.value.Value;

import java.util.LinkedHashMap;
import java.util.Map;

@Value.Immutable
public interface ConnectorMetadata {
  @Value.Derived
  default Map<String, String> labels() {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("io.confluent.csid.kafka.connect.k8s.connector", connectorName());
    labels.put("io.confluent.csid.kafka.connect.k8s.cluster", cluster());
    return labels;
  }

  String cluster();

  int taskCount();

  String namespace();

  String connectorName();

  @Value.Derived
  default V1ObjectMeta configMetaTask() {
    return new V1ObjectMeta()
        .namespace(namespace())
        .name(
            String.format("%s-task", connectorName())
        ).labels(labels());
  }

  @Value.Derived
  default V1ObjectMeta configMetaConnector() {
    return new V1ObjectMeta()
        .namespace(namespace())
        .name(
            String.format("%s-connector", connectorName())
        ).labels(labels());
  }

  default V1StatefulSet connectorStatefulSet() {
    V1StatefulSet connectorStatefulSet = new V1StatefulSet();
    connectorStatefulSet.setMetadata(configMetaConnector());
    V1StatefulSetSpec spec = new V1StatefulSetSpec();
    spec.setServiceName(connectorStatefulSet.getMetadata().getName());
    spec.replicas(1);
    V1LabelSelector labelSelector = new V1LabelSelector();
    labelSelector.setMatchLabels(labels());
    spec.selector(labelSelector);
    spec.updateStrategy(
        new V1StatefulSetUpdateStrategy()
            .type("RollingUpdate")
    );
    connectorStatefulSet.setSpec(spec);
    V1PodSpec podSpec = new V1PodSpec();
    V1PodTemplateSpec templateSpec = new V1PodTemplateSpec();
    templateSpec.spec(podSpec);
    templateSpec.metadata(new V1ObjectMeta().labels(connectorStatefulSet.getMetadata().getLabels()));
    spec.setTemplate(templateSpec);

    V1Volume configVolume = new V1Volume()
        .name("config")
        .configMap(new V1ConfigMapVolumeSource().name(configMetaConnector().getName()));
    podSpec.addVolumesItem(configVolume);

    V1Container container = new V1Container()
        .name("connector")
        .image("ubuntu:latest")
        .addCommandItem("/bin/bash")
        .addCommandItem("-c")
        .addCommandItem("--")
        .addArgsItem("while true; do sleep 1; done;");


    V1VolumeMount volumeMount = new V1VolumeMount()
        .name(configVolume.getName())
        .mountPath("/config");
    container.addVolumeMountsItem(volumeMount);
    podSpec.addContainersItem(container);
    return connectorStatefulSet;
  }

  default V1StatefulSet taskStatefulSet() {
    V1StatefulSet connectorStatefulSet = new V1StatefulSet();
    connectorStatefulSet.setMetadata(configMetaTask());
    V1StatefulSetSpec spec = new V1StatefulSetSpec();
    spec.setServiceName(connectorStatefulSet.getMetadata().getName());
    spec.replicas(taskCount());
    V1LabelSelector labelSelector = new V1LabelSelector();
    labelSelector.setMatchLabels(labels());
    spec.selector(labelSelector);
    spec.updateStrategy(
        new V1StatefulSetUpdateStrategy()
            .type("RollingUpdate")
    );
    connectorStatefulSet.setSpec(spec);
    V1PodSpec podSpec = new V1PodSpec();
    V1PodTemplateSpec templateSpec = new V1PodTemplateSpec();
    templateSpec.spec(podSpec);
    templateSpec.metadata(new V1ObjectMeta().labels(connectorStatefulSet.getMetadata().getLabels()));
    spec.setTemplate(templateSpec);

    V1Volume configVolume = new V1Volume()
        .name("config")
        .configMap(new V1ConfigMapVolumeSource().name(configMetaConnector().getName()));
    podSpec.addVolumesItem(configVolume);

    V1Container container = new V1Container()
        .name("connector")
        .image("ubuntu:latest")
        .addCommandItem("/bin/bash")
        .addCommandItem("-c")
        .addCommandItem("--")
        .addArgsItem("while true; do sleep 1; done;");


    V1VolumeMount volumeMount = new V1VolumeMount()
        .name(configVolume.getName())
        .mountPath("/config");
    container.addVolumeMountsItem(volumeMount);
    podSpec.addContainersItem(container);
    return connectorStatefulSet;
  }
}
