package io.confluent.csid.kafka.connect.k8s.operator;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

public class ConnectorReconciler implements Reconciler<Connector> {
  @Override
  public UpdateControl<Connector> reconcile(Connector connector, Context<Connector> context) throws Exception {
    return null;
  }
}
