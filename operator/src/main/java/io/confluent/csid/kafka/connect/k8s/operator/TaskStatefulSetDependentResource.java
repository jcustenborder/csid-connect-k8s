package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.confluent.csid.kafka.connect.k8s.common.Utils.ifPresent;
import static io.confluent.csid.kafka.connect.k8s.operator.LabelMaker.addTaskLabels;
import static io.javaoperatorsdk.operator.ReconcilerUtils.loadYaml;

@KubernetesDependent(labelSelector = LabelMaker.SELECTOR_TASK)
public class TaskStatefulSetDependentResource extends CRUDKubernetesDependentResource<StatefulSet, KafkaConnector> {
  private static final Logger log = LoggerFactory.getLogger(TaskStatefulSetDependentResource.class);

  public TaskStatefulSetDependentResource() {
    super(StatefulSet.class);
  }

  @Override
  protected StatefulSet desired(KafkaConnector primary, Context<KafkaConnector> context) {
    KafkaConnectorState state = ImmutableKafkaConnectorState.builder()
        .kafkaConnector(primary)
        .build();

    Set<ConfigMap> taskConfigMaps = context.getSecondaryResources(ConfigMap.class);
    log.trace("desired() - found {} ConfigMaps", taskConfigMaps);

    ConfigMap taskConfigMap = taskConfigMaps.stream().filter(e -> e.getMetadata().getName().equals(state.taskConfigMapName())).findFirst().orElseThrow(() ->
        new IllegalStateException(String.format("Could not find %s", state.taskConfigMapName()))
    );

    StatefulSet statefulSet = loadYaml(StatefulSet.class, this.getClass(), "statefulset.tasks.yml");

    statefulSet.setMetadata(state.taskStatefulSet().build());
    addTaskLabels(statefulSet.getSpec().getSelector().getMatchLabels(), primary);
    addTaskLabels(statefulSet.getSpec().getTemplate().getMetadata().getLabels(), primary);
    statefulSet.getSpec().setReplicas(taskConfigMap.getData().size());
    statefulSet.getSpec().getTemplate().getSpec().setVolumes(
        List.of(
            state.connectorConfigMapVolume(),
            state.workerConfigMapVolume(),
            state.taskConfigMapVolume()
        )
    );

    PodSpec podSpec = statefulSet.getSpec().getTemplate().getSpec();
    ifPresent(primary.getSpec().getImagePullSecrets(), podSpec::setImagePullSecrets);

    Container container = statefulSet.getSpec().getTemplate().getSpec().getContainers().get(0);
    container.setImage(primary.getSpec().getTask().getImage());

    ResourceRequirements resourceRequirements = primary.getSpec().getTask().resourceRequirements();
    if (null != resourceRequirements) {
      container.setResources(resourceRequirements);
    }

//    container.setImage(primary.getSpec().getConnectorImage());
    ifPresent(primary.getSpec().getImagePullPolicy(), container::setImagePullPolicy);
    container.setVolumeMounts(
        List.of(
            state.connectorConfigMapVolumeMount(),
            state.workerConfigMapVolumeMount(),
            state.taskConfigMapVolumeMount()
        )
    );

    return statefulSet;
  }

  @Override
  public void delete(KafkaConnector primary, Context<KafkaConnector> context) {
    super.delete(primary, context);
    KafkaConnectorState state = ImmutableKafkaConnectorState.builder()
        .kafkaConnector(primary)
        .build();

    Set<ConfigMap> taskConfigMaps = context.getSecondaryResources(ConfigMap.class);
    Optional<ConfigMap> taskConfigMap = taskConfigMaps.stream().filter(e -> e.getMetadata().getName().equals(state.taskConfigMapName())).findFirst();

    if (taskConfigMap.isPresent()) {
      if (null != primary.getMetadata().getNamespace()) {
        getKubernetesClient().configMaps().inNamespace(primary.getMetadata().getNamespace())
            .delete(taskConfigMap.get());
      } else {
        getKubernetesClient().configMaps()
            .delete(taskConfigMap.get());
      }
    }
  }
}
