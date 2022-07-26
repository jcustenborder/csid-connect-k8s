package io.confluent.csid.kafka.connect.k8s.operator;

import io.confluent.csid.kafka.connect.k8s.AbstractRunnable;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.config.runtime.DefaultConfigurationService;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperatorRunnable extends AbstractRunnable {
  private static final Logger log = LoggerFactory.getLogger(OperatorRunnable.class);
  public OperatorRunnable(Namespace namespace) {
    super(namespace);
  }

  @Override
  public void run() {
    log.info("Starting ....");


    Operator operator = new Operator();









  }
}
