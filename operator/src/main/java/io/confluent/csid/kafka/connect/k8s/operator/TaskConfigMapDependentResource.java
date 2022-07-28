package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.SecondaryToPrimaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@KubernetesDependent(labelSelector = "managedby=connector")
public class TaskConfigMapDependentResource extends KubernetesDependentResource<ConfigMap, KafkaConnector> implements SecondaryToPrimaryMapper<ConfigMap> {
  private static final Logger log = LoggerFactory.getLogger(TaskConfigMapDependentResource.class);
  public TaskConfigMapDependentResource() {
    super(ConfigMap.class);
  }

  @Override
  public Set<ResourceID> toPrimaryResourceIDs(ConfigMap configMap) {
    Set<ResourceID> result = new LinkedHashSet<>();
    if (null != configMap.getMetadata().getLabels() && !configMap.getMetadata().getLabels().isEmpty()) {
      String connector = configMap.getMetadata().getLabels().get("connector");
      result.add(
          new ResourceID(connector, configMap.getMetadata().getNamespace())
      );
    }
    return result;
  }

  @Override
  protected void onUpdated(ResourceID primaryResourceId, ConfigMap updated, ConfigMap actual) {
    super.onUpdated(primaryResourceId, updated, actual);

    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("type", "task");
    labels.put("connector", primaryResourceId.getName());

    if (primaryResourceId.getNamespace().isPresent()) {
      PodList podList =
          getKubernetesClient().pods().inNamespace(primaryResourceId.getNamespace().get())
              .withLabels(labels)
              .list();
      for (Pod pod : podList.getItems()) {
        log.info("onUpdated() - Restarting pod {}", pod.getMetadata().getName());
        getKubernetesClient().pods().inNamespace(primaryResourceId.getNamespace().get()).delete(pod);
      }
    } else {
      PodList podList =
          getKubernetesClient().pods()
              .withLabels(labels)
              .list();
      for (Pod pod : podList.getItems()) {
        log.info("onUpdated() - Restarting pod {}", pod.getMetadata().getName());
        getKubernetesClient().pods().delete(pod);
      }
    }


  }
}
