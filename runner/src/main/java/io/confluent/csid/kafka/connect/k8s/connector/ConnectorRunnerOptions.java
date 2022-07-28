package io.confluent.csid.kafka.connect.k8s.connector;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class ConnectorRunnerOptions {
  private static final Logger log = LoggerFactory.getLogger(ConnectorRunnerOptions.class);
  public final File connectorDirectory;
  public final File workerDirectory;


  private ConnectorRunnerOptions(File configDir, File workerDirectory) {
    this.connectorDirectory = configDir;
    this.workerDirectory = workerDirectory;
  }


  public static ConnectorRunnerOptions create(String... args) throws Exception {
    ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("connector-runner");
    argumentParser.addArgument("--connector")
        .type(File.class)
        .required(true)
        .help("Location on the file system to load the connector configuration.");
    argumentParser.addArgument("--worker")
        .type(File.class)
        .required(true)
        .help("Location on the file system to load the worker configuration files from.");


    try {
      Namespace parserNamespace = argumentParser.parseArgs(args);
      File connectorDirectory = parserNamespace.get("connector");
      File workerDirectory = parserNamespace.get("worker");

      if (!connectorDirectory.isDirectory()) {
        throw new FileNotFoundException("--connector must be a directory");
      }
      if (!workerDirectory.isDirectory()) {
        throw new FileNotFoundException("--worker must be a directory");
      }

      return new ConnectorRunnerOptions(connectorDirectory, workerDirectory);
    } catch (ArgumentParserException e) {
      argumentParser.handleError(e);
      System.exit(0);
    }


    return null;
  }

  private Map<String, String> loadSettings(File file) throws IOException {
    log.info("loadSettings() - Attempting to load settings from {}", file);
    Properties properties = Utils.loadProps(file.getAbsolutePath());
    return Utils.propsToStringMap(properties);
  }

  public Map<String, String> workerSettings() throws IOException {
    File taskFile = new File(this.workerDirectory, "worker.properties");
    return loadSettings(taskFile);
  }

  public Map<String, String> connectorSettings() throws IOException {
    File taskFile = new File(this.connectorDirectory, "connector.properties");
    return loadSettings(taskFile);
  }

  public Map<String, String> k8sSettings() throws IOException {
    File taskFile = new File(this.connectorDirectory, "k8s.properties");
    return loadSettings(taskFile);
  }
}
