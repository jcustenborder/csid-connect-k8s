package io.confluent.csid.kafka.connect.k8s.task;

import io.confluent.csid.kafka.connect.k8s.AbstractRunnable;
import net.sourceforge.argparse4j.inf.Namespace;

public class TaskRunnable extends AbstractRunnable {
  public TaskRunnable(Namespace namespace) {
    super(namespace);
  }

  @Override
  public void run() {

  }
}
