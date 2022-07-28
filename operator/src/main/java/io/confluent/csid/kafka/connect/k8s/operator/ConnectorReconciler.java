package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@ControllerConfiguration(
    dependents = {
        @Dependent(type = ConnectorConfigMapDependentResource.class, name = "connector-configmap"),
        @Dependent(type = ConnectorStatefulSetDependentResource.class, name = "connector-statefulset"),
        @Dependent(type = TaskConfigMapDependentResource.class, name = "task-configmap"),
        @Dependent(type = TaskStatefulSetDependentResource.class, name = "task-statefulset", dependsOn = {"connector-statefulset"})
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
