package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.confluent.csid.kafka.connect.k8s.common.Utils;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSetSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@KubernetesDependent(labelSelector = "type=task")
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

    ConfigMap taskConfigMap = taskConfigMaps.stream().filter(e->e.getMetadata().getName().equals(state.taskConfigMapName())).findFirst().orElseThrow(() ->
       new IllegalStateException(String.format("Could not find %s", state.taskConfigMapName()))
    );

    ContainerBuilder containerBuilder = new ContainerBuilder()
        .withName("task")
        .withImage(primary.getSpec().getTaskImage())
        .withVolumeMounts(
            state.taskConfigMapVolumeMount(),
            state.workerConfigMapVolumeMount(),
            state.connectorConfigMapVolumeMount()
        )
        .withCommand("/usr/bin/task-runner")
        .withArgs(
            "--connector",
            "/config/connector",
            "--task",
            "/config/task",
            "--worker",
            "/config/worker"
        );

    Utils.ifPresent(
        primary.getSpec().getImagePullPolicy(),
        containerBuilder::withImagePullPolicy
    );

    PodSpecBuilder podSpecBuilder = new PodSpecBuilder()
        .withVolumes(
            state.taskConfigMapVolume(),
            state.workerConfigMapVolume(),
            state.connectorConfigMapVolume()
        )
//        .withServiceAccountName("connector-operator") //This needs to be configurable
        .withContainers(containerBuilder.build());

    Utils.ifPresent(primary.getSpec().getImagePullSecrets(), podSpecBuilder::withImagePullSecrets);

    return new StatefulSetBuilder()
        .withMetadata(
            state.taskStatefulSet().addToLabels("type", "task").build()
        ).withSpec(
            new StatefulSetSpecBuilder()
                .withServiceName(state.taskStatefulSet().getName())
                .withSelector(
                    new LabelSelectorBuilder()
                        .withMatchLabels(state.connectorMatchLabels())
                        .build()
                )
                .withReplicas(taskConfigMap.getData().size())
                .withTemplate(
                    new PodTemplateSpecBuilder()
                        .withMetadata(
                            new ObjectMetaBuilder()
                                .withLabels(state.labels())
                                .build()
                        )
                        .withSpec(
                            podSpecBuilder.build()
                        )
                        .build()
                )
                .build()
        )
        .build();
  }
}
