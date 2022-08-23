package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.KafkaConnector;
import io.fabric8.kubernetes.api.model.Service;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent(labelSelector = LabelMaker.SELECTOR_TASK)
public class TaskServiceDependantResource extends CRUDKubernetesDependentResource<Service, KafkaConnector> {
  public TaskServiceDependantResource() {
    super(Service.class);
  }

  @Override
  protected Service desired(KafkaConnector primary, Context<KafkaConnector> context) {



    return null;
  }


}
