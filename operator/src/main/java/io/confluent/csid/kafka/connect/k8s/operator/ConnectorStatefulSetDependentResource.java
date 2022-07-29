package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.ExecActionBuilder;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.LifecycleBuilder;
import io.fabric8.kubernetes.api.model.LifecycleHandlerBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PodSpec;
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

import java.util.List;

import static io.confluent.csid.kafka.connect.k8s.common.Utils.ifPresent;
import static io.confluent.csid.kafka.connect.k8s.operator.LabelMaker.addConnectorLabels;
import static io.javaoperatorsdk.operator.ReconcilerUtils.loadYaml;

@KubernetesDependent(labelSelector = LabelMaker.SELECTOR_CONNECTOR)
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

    StatefulSet statefulSet = loadYaml(StatefulSet.class, this.getClass(), "statefulset.connector.yml");
    //TODO: this needs to be a config setting.
    statefulSet.getSpec().getTemplate().getSpec().setServiceAccountName("connector-operator");
    statefulSet.setMetadata(state.connectorStatefulSet().build());
    addConnectorLabels(statefulSet.getSpec().getSelector().getMatchLabels(), primary);
    addConnectorLabels(statefulSet.getSpec().getTemplate().getMetadata().getLabels(), primary);

    statefulSet.getSpec().getTemplate().getSpec().setVolumes(
        List.of(
            state.connectorConfigMapVolume(),
            state.workerConfigMapVolume()
        )
    );

    PodSpec podSpec = statefulSet.getSpec().getTemplate().getSpec();
    ifPresent(primary.getSpec().getImagePullSecrets(), podSpec::setImagePullSecrets);

    Container container = statefulSet.getSpec().getTemplate().getSpec().getContainers().get(0);
    container.setImage(primary.getSpec().getConnectorImage());
    ifPresent(primary.getSpec().getImagePullPolicy(), container::setImagePullPolicy);
    container.setVolumeMounts(
        List.of(
            state.connectorConfigMapVolumeMount(),
            state.workerConfigMapVolumeMount()
        )
    );

    return statefulSet;
  }
}
