package io.confluent.csid.kafka.connect.k8s.operator;

import io.javaoperatorsdk.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    log.info("Starting ....");
    Operator operator = new Operator();
    operator.register(new ConnectorReconciler());
    operator.installShutdownHook();
    operator.start();
  }
}
