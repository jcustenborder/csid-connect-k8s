package io.confluent.csid.kafka.connect.k8s;

import net.sourceforge.argparse4j.inf.Namespace;

public abstract class AbstractRunnable implements Runnable {
  protected final Namespace namespace;

  protected AbstractRunnable(Namespace namespace) {
    this.namespace = namespace;

  }

  protected void downloadPlugins() {

  }
}
