package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.confluent.csid.kafka.connect.k8s.common.Utils;
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

@KubernetesDependent(labelSelector = "type=connector")
public class ConnectorStatefulSetDependentResource extends CRUDKubernetesDependentResource<StatefulSet, KafkaConnector> {
  private static final Logger log = LoggerFactory.getLogger(ConnectorStatefulSetDependentResource.class);

  public ConnectorStatefulSetDependentResource() {
    super(StatefulSet.class);
  }


  @Override
  protected StatefulSet desired(KafkaConnector primary, Context<KafkaConnector> context) {
    KafkaConnectorState state = ImmutableKafkaConnectorState.builder()
        .kafkaConnector(primary)
        .build();
    ContainerBuilder containerBuilder = new ContainerBuilder()
        .withName("connector")
        .withImage(primary.getSpec().getConnectorImage())
        .withVolumeMounts(
            state.connectorConfigMapVolumeMount(),
            state.workerConfigMapVolumeMount()
        )
        .withCommand("/usr/bin/connector-runner")
        .withArgs(
            "--connector",
            "/config/connector",
            "--worker",
            "/config/worker"
        );

    Utils.ifPresent(
        primary.getSpec().getImagePullPolicy(),
        containerBuilder::withImagePullPolicy
    );

    PodSpecBuilder podSpecBuilder = new PodSpecBuilder()
        .withVolumes(
            state.connectorConfigMapVolume(),
            state.workerConfigMapVolume()
        )
        .withServiceAccountName("connector-operator") //This needs to be configurable
        .withContainers(containerBuilder.build());

    Utils.ifPresent(primary.getSpec().getImagePullSecrets(), podSpecBuilder::withImagePullSecrets);

    return new StatefulSetBuilder()
        .withMetadata(
            state.connectorStatefulSet().addToLabels("type", "connector").build()
        ).withSpec(
            new StatefulSetSpecBuilder()
                .withServiceName(state.connectorStatefulSet().getName())
                .withSelector(
                    new LabelSelectorBuilder()
                        .withMatchLabels(state.connectorMatchLabels())
                        .build()
                )
                .withReplicas(1)
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
