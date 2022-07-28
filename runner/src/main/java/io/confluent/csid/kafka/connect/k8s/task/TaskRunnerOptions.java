package io.confluent.csid.kafka.connect.k8s.task;

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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskRunnerOptions {
  private static final Logger log = LoggerFactory.getLogger(TaskRunnerOptions.class);
  public final File connectorDirectory;
  public final File workerDirectory;

  public final File taskDirectory;

  public final int taskIndex;


  private TaskRunnerOptions(File configDir, File workerDirectory, File taskDirectory, int taskIndex) {
    this.connectorDirectory = configDir;
    this.workerDirectory = workerDirectory;
    this.taskDirectory = taskDirectory;
    this.taskIndex = taskIndex;
  }


  public static TaskRunnerOptions create(String... args) throws Exception {

    log.trace("Determining hostname");
    String defaultHostname;
    try {
      InetAddress inetAddress = InetAddress.getLocalHost();
      defaultHostname = inetAddress.getHostName();
    } catch (UnknownHostException e) {
      defaultHostname = "unknown";
    }

    ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("task-runner");
    argumentParser.addArgument("--task")
        .type(File.class)
        .required(true)
        .help("Location on the file system to load the task configuration.");
    argumentParser.addArgument("--connector")
        .type(File.class)
        .required(true)
        .help("Location on the file system to load the connector configuration.");
    argumentParser.addArgument("--worker")
        .type(File.class)
        .required(true)
        .help("Location on the file system to load the worker configuration files from.");
    argumentParser.addArgument("--hostname")
        .type(String.class)
        .setDefault(defaultHostname)
        .help("Location on the file system to load the worker configuration files from.");


    try {
      Namespace parserNamespace = argumentParser.parseArgs(args);
      File connectorDirectory = parserNamespace.get("connector");
      File workerDirectory = parserNamespace.get("worker");
      File taskDirectory = parserNamespace.get("task");
      String hostname = parserNamespace.get("hostname");

      if (!connectorDirectory.isDirectory()) {
        throw new FileNotFoundException("--connector must be a directory");
      }
      if (!workerDirectory.isDirectory()) {
        throw new FileNotFoundException("--worker must be a directory");
      }

      Pattern hostnamePattern = Pattern.compile("^.+(\\d+)$");
      Matcher matcher = hostnamePattern.matcher(hostname);

      if(!matcher.matches()) {
        throw new IllegalArgumentException(
            String.format("Hostname '%s' could not be matched to pattern '%s'.", hostname, hostnamePattern.pattern())
        );
      }

      int taskIndex = Integer.parseInt(matcher.group(1));

      return new TaskRunnerOptions(connectorDirectory, workerDirectory, taskDirectory, taskIndex);
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

  public Map<String, String> taskSettings() throws IOException {
    File taskFile = new File(this.taskDirectory, String.format("task-%s.properties", this.taskIndex));
    return loadSettings(taskFile);
  }

}
