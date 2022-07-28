package io.confluent.csid.kafka.connect.k8s.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PluginDownloader implements Closeable {
  private static final Logger log = LoggerFactory.getLogger(PluginDownloader.class);
  final K8sConfig config;

  public PluginDownloader(K8sConfig config) {
    this.config = config;
  }

  static final Pattern PATTERN = Pattern.compile("^([^/]+)/([^:]+):(.+)$");

  @Value.Immutable
  interface DownloadItem {
    String owner();

    String plugin();

    String version();

    @Value.Derived
    default String url() {
      return String.format("https://api.hub.confluent.io/api/plugins/%s/%s/versions/%s", owner(), plugin(), version());
    }

    @Value.Derived
    default String pluginDirectoryName() {
      return String.format("%s-%s", owner(), plugin());
    }
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableConnectHubDownloadVersion.class)
  interface ConnectHubDownloadVersion {
    @JsonProperty("archive")
    Archive archive();

    @Value.Immutable
    @JsonDeserialize(as = ImmutableArchive.class)
    interface Archive {
      @JsonProperty("name")
      String name();

      @JsonProperty("url")
      String url();

      @JsonProperty("sha1")
      String sha1();
    }
  }

  public void download() throws IOException {
    Map<DownloadItem, ConnectHubDownloadVersion> downloadItems = new LinkedHashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    for (String download : config.pluginDownload) {
      Matcher matcher = PATTERN.matcher(download);
      if (matcher.matches()) {
        DownloadItem item = ImmutableDownloadItem.builder()
            .owner(matcher.group(1))
            .plugin(matcher.group(2))
            .version(matcher.group(3))
            .build();

        URL url = new URL(item.url());
        ConnectHubDownloadVersion downloadVersion;
        log.info("Downloading version information from {}", url);
        try (InputStream inputStream = url.openStream()) {
          downloadVersion = mapper.readValue(inputStream, ConnectHubDownloadVersion.class);
        }
        log.info("Adding {} {} to the downloads.", item, downloadVersion);
        downloadItems.put(item, downloadVersion);
      } else {
        throw new IllegalStateException(
            String.format("Could not match '%s' to pattern(%s)", download, PATTERN.pattern())
        );
      }
    }

    if (downloadItems.isEmpty()) {
      log.warn("No plugins to download");
      return;
    }

    File tempPath = new File("/tmp");

    File pluginRootPath = new File("/usr/share/confluent-hub-components");

    if (!pluginRootPath.isDirectory()) {
      pluginRootPath.mkdirs();
    }

    for (Map.Entry<DownloadItem, ConnectHubDownloadVersion> e : downloadItems.entrySet()) {
      File downloadPath = new File(tempPath, e.getValue().archive().name());
      File extractRoot = new File(pluginRootPath, e.getKey().pluginDirectoryName());
      URL url = new URL(e.getValue().archive().url());
      byte[] buffer = new byte[64 * 1024];

      log.info("Downloading {} to {}", url, downloadPath);
      try (InputStream inputStream = url.openStream()) {
        try (OutputStream outputStream = Files.newOutputStream(downloadPath.toPath())) {
          int length = 0;
          while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
          }
        }
      }

      try (InputStream inputStream = Files.newInputStream(downloadPath.toPath())) {
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
          ZipEntry zipEntry;

          while (null != (zipEntry = zipInputStream.getNextEntry())) {
            File outputFile = new File(extractRoot, zipEntry.getName());
            log.debug("Extracting {} to {}", zipEntry.getName(), outputFile);

            if (!zipEntry.isDirectory()) {
              if (!outputFile.getParentFile().isDirectory()) {
                outputFile.getParentFile().mkdirs();
              }
              try (OutputStream outputStream = Files.newOutputStream(outputFile.toPath(), StandardOpenOption.CREATE)) {
                int read;

                while ((read = zipInputStream.read(buffer, 0, buffer.length)) != -1) {
                  outputStream.write(buffer, 0, read);
                }
              }
            }
          }
        }
      }
    }


  }


  @Override
  public void close() throws IOException {

  }
}
