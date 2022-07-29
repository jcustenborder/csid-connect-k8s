package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Cleaner;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.DeleteControl;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerConfiguration(
    dependents = {
        @Dependent(type = ConnectorConfigMapDependentResource.class, name = "connector-configmap"),
        @Dependent(type = ConnectorStatefulSetDependentResource.class, name = "connector-statefulset", dependsOn = {"connector-configmap"}),
        @Dependent(type = TaskConfigMapDependentResource.class, name = "task-configmap", dependsOn = {"connector-statefulset"}),
        @Dependent(type = TaskStatefulSetDependentResource.class, name = "task-statefulset", dependsOn = {"task-configmap"})
    }
)
public class ConnectorReconciler implements Reconciler<KafkaConnector> {
  private static final Logger log = LoggerFactory.getLogger(ConnectorReconciler.class);

  @Override
  public UpdateControl<KafkaConnector> reconcile(KafkaConnector connector, Context<KafkaConnector> context) throws Exception {
    log.info("reconcile() - connector = {}", connector);

    Set<ConfigMap> configMaps = context.getSecondaryResources(ConfigMap.class);
    Set<StatefulSet> statefulSets = context.getSecondaryResources(StatefulSet.class);

    return UpdateControl.noUpdate();
  }
}
