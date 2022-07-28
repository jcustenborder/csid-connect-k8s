package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.SecondaryToPrimaryMapper;

import java.util.LinkedHashSet;
import java.util.Set;

@KubernetesDependent(labelSelector = "managedby=connector")
public class TaskConfigMapDependentResource extends KubernetesDependentResource<ConfigMap, KafkaConnector> implements SecondaryToPrimaryMapper<ConfigMap> {
  public TaskConfigMapDependentResource() {
    super(ConfigMap.class);
  }

  @Override
  public Set<ResourceID> toPrimaryResourceIDs(ConfigMap configMap) {
    String connector = configMap.getMetadata().getLabels().get("connector");

    Set<ResourceID> resourceIDS = new LinkedHashSet<>();
    resourceIDS.add(
        new ResourceID(connector, configMap.getMetadata().getNamespace())
    );
    return resourceIDS;
  }
}
