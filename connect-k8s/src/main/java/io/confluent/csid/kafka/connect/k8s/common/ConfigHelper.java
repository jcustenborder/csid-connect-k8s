package io.confluent.csid.kafka.connect.k8s.common;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.util.ConnectorTaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigHelper {
  private static final Logger log = LoggerFactory.getLogger(ConfigHelper.class);
  public final File configDirectory;
  public final String hostname;
  public final Optional<Integer> taskIndex;

  public final String connectorName;
  public final String processType;

  ConfigHelper(File configDirectory, String hostname, Optional<Integer> taskIndex, String connectorName, String processType) {
    this.configDirectory = configDirectory;
    this.hostname = hostname;
    this.taskIndex = taskIndex;
    this.connectorName = connectorName;
    this.processType = processType;
  }

  private Map<String, String> loadSettings(File file) throws IOException {
    log.info("loadSettings() - Attempting to load settings from {}", file);
    Properties properties = Utils.loadProps(file.getAbsolutePath());
    return Utils.propsToStringMap(properties);
  }

  public Map<String, String> taskSettings() throws IOException {
    int i = this.taskIndex.orElseThrow(() -> new IllegalStateException("taskIndex not set."));
    String taskFileName = String.format("task-%s.properties", i);
    File taskFile = new File(this.configDirectory, taskFileName);
    return loadSettings(taskFile);
  }


  public static ConfigHelper create(String... args) throws Exception {
    ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("worker");
    argumentParser.addArgument("-c", "--config-dir").type(File.class).required(true);
    argumentParser.addArgument("-H", "--hostname").type(String.class);

    Namespace namespace = argumentParser.parseArgs(args);

    String hostname = namespace.getString("hostname");

    if (null == hostname || hostname.isEmpty()) {
      log.trace("Determining hostname");

      try {
        InetAddress inetAddress = InetAddress.getLocalHost();
        hostname = inetAddress.getHostName();
      } catch (UnknownHostException e) {
        hostname = "unknown";
      }
    }
    log.info("hostname = {}", hostname);

    File configDirectory = namespace.get("config_dir");

    if (!configDirectory.isAbsolute()) {
      configDirectory = configDirectory.getAbsoluteFile();
    }

    log.info("configDirectory = {}", configDirectory);

    if (!configDirectory.isDirectory()) {
      throw new FileNotFoundException(
          String.format("Config directory not found: %s", configDirectory)
      );
    }


    Pattern hostnamePattern = Pattern.compile("^([a-z\\d-]+)-(operator|connector|task)-(\\d+)$");
    Matcher matcher = hostnamePattern.matcher(hostname);

    String connectorName;
    String processType;
    Optional<Integer> taskIndex;

    if (matcher.matches()) {
      connectorName = matcher.group(1);
      processType = matcher.group(2);
      if (processType.equalsIgnoreCase("task")) {
        Integer i = Integer.parseInt(matcher.group(3));
        taskIndex = Optional.of(i);
      } else {
        taskIndex = Optional.empty();
      }
    } else {
      throw new IllegalStateException(
          String.format("Could not match '%s' against regex '%s'", hostname, hostnamePattern.pattern())
      );
    }

    return new ConfigHelper(configDirectory, hostname, taskIndex, connectorName, processType);
  }

  public Map<String, String> workerSettings() throws IOException {
    File taskFile = new File(this.configDirectory, "worker.properties");
    return loadSettings(taskFile);
  }

  public Map<String, String> connectorSettings() throws IOException {
    File taskFile = new File(this.configDirectory, "connector.properties");
    return loadSettings(taskFile);
  }

  public ConnectorTaskId taskId() {
    return new ConnectorTaskId(this.connectorName, this.taskIndex.get());
  }


}
