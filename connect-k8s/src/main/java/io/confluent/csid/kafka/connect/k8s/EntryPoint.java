package io.confluent.csid.kafka.connect.k8s;

import io.confluent.csid.kafka.connect.k8s.connector.ConnectorRunnable;
import io.confluent.csid.kafka.connect.k8s.operator.OperatorRunnable;
import io.confluent.csid.kafka.connect.k8s.task.TaskRunnable;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
  private static final Logger log = LoggerFactory.getLogger(EntryPoint.class);

  public static void main(String[] args) {
    ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("entry", true);
    Subparsers subparsers = argumentParser.addSubparsers();

    Subparser connectorSubparser = subparsers.addParser("connector")
        .description("Entry point to start a connector launched by the operator.")
        .setDefault("command", "connector");
    Subparser taskSubparser = subparsers.addParser("task")
        .description("Entry point to start a task launched by a connector.")
        .setDefault("command", "task");
    Subparser operator = subparsers.addParser("operator");

    final Namespace namespace;
    try {
      namespace = argumentParser.parseArgs(args);
      log.trace("{}", namespace);
    } catch (ArgumentParserException e) {
      argumentParser.handleError(e);
      System.exit(1);
      return;
    }

    String command = namespace.getString("command");

    AbstractRunnable runnable;

    switch (command) {
      case "connector":
        runnable = new ConnectorRunnable(namespace);
        break;
      case "task":
        runnable = new TaskRunnable(namespace);
        break;
      case "operator":
        runnable = new OperatorRunnable(namespace);
        break;
      default:
        throw new IllegalStateException();
    }

    log.info("Starting {}", runnable);
    runnable.run();

  }
}
