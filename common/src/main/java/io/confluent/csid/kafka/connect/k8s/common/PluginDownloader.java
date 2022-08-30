/**
 *                     Copyright Confluent
 *                     Confluent Community License Agreement
 *                                 Version 1.0
 *
 * This Confluent Community License Agreement Version 1.0 (the “Agreement”) sets
 * forth the terms on which Confluent, Inc. (“Confluent”) makes available certain
 * software made available by Confluent under this Agreement (the “Software”).  BY
 * INSTALLING, DOWNLOADING, ACCESSING, USING OR DISTRIBUTING ANY OF THE SOFTWARE,
 * YOU AGREE TO THE TERMS AND CONDITIONS OF THIS AGREEMENT. IF YOU DO NOT AGREE TO
 * SUCH TERMS AND CONDITIONS, YOU MUST NOT USE THE SOFTWARE.  IF YOU ARE RECEIVING
 * THE SOFTWARE ON BEHALF OF A LEGAL ENTITY, YOU REPRESENT AND WARRANT THAT YOU
 * HAVE THE ACTUAL AUTHORITY TO AGREE TO THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT ON BEHALF OF SUCH ENTITY.  “Licensee” means you, an individual, or
 * the entity on whose behalf you are receiving the Software.
 *
 *    1. LICENSE GRANT AND CONDITIONS.
 *
 *       1.1 License.  Subject to the terms and conditions of this Agreement,
 *       Confluent hereby grants to Licensee a non-exclusive, royalty-free,
 *       worldwide, non-transferable, non-sublicenseable license during the term
 *       of this Agreement to: (a) use the Software; (b) prepare modifications and
 *       derivative works of the Software; (c) distribute the Software (including
 *       without limitation in source code or object code form); and (d) reproduce
 *       copies of the Software (the “License”).  Licensee is not granted the
 *       right to, and Licensee shall not, exercise the License for an Excluded
 *       Purpose.  For purposes of this Agreement, “Excluded Purpose” means making
 *       available any software-as-a-service, platform-as-a-service,
 *       infrastructure-as-a-service or other similar online service that competes
 *       with Confluent products or services that provide the Software.
 *
 *       1.2 Conditions.  In consideration of the License, Licensee’s distribution
 *       of the Software is subject to the following conditions:
 *
 *          (a) Licensee must cause any Software modified by Licensee to carry
 *          prominent notices stating that Licensee modified the Software.
 *
 *          (b) On each Software copy, Licensee shall reproduce and not remove or
 *          alter all Confluent or third party copyright or other proprietary
 *          notices contained in the Software, and Licensee must provide the
 *          notice below with each copy.
 *
 *             “This software is made available by Confluent, Inc., under the
 *             terms of the Confluent Community License Agreement, Version 1.0
 *             located at http://www.confluent.io/confluent-community-license.  BY
 *             INSTALLING, DOWNLOADING, ACCESSING, USING OR DISTRIBUTING ANY OF
 *             THE SOFTWARE, YOU AGREE TO THE TERMS OF SUCH LICENSE AGREEMENT.”
 *
 *       1.3 Licensee Modifications.  Licensee may add its own copyright notices
 *       to modifications made by Licensee and may provide additional or different
 *       license terms and conditions for use, reproduction, or distribution of
 *       Licensee’s modifications.  While redistributing the Software or
 *       modifications thereof, Licensee may choose to offer, for a fee or free of
 *       charge, support, warranty, indemnity, or other obligations. Licensee, and
 *       not Confluent, will be responsible for any such obligations.
 *
 *       1.4 No Sublicensing.  The License does not include the right to
 *       sublicense the Software, however, each recipient to which Licensee
 *       provides the Software may exercise the Licenses so long as such recipient
 *       agrees to the terms and conditions of this Agreement.
 *
 *    2. TERM AND TERMINATION.  This Agreement will continue unless and until
 *    earlier terminated as set forth herein.  If Licensee breaches any of its
 *    conditions or obligations under this Agreement, this Agreement will
 *    terminate automatically and the License will terminate automatically and
 *    permanently.
 *
 *    3. INTELLECTUAL PROPERTY.  As between the parties, Confluent will retain all
 *    right, title, and interest in the Software, and all intellectual property
 *    rights therein.  Confluent hereby reserves all rights not expressly granted
 *    to Licensee in this Agreement.  Confluent hereby reserves all rights in its
 *    trademarks and service marks, and no licenses therein are granted in this
 *    Agreement.
 *
 *    4. DISCLAIMER.  CONFLUENT HEREBY DISCLAIMS ANY AND ALL WARRANTIES AND
 *    CONDITIONS, EXPRESS, IMPLIED, STATUTORY, OR OTHERWISE, AND SPECIFICALLY
 *    DISCLAIMS ANY WARRANTY OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR
 *    PURPOSE, WITH RESPECT TO THE SOFTWARE.
 *
 *    5. LIMITATION OF LIABILITY.  CONFLUENT WILL NOT BE LIABLE FOR ANY DAMAGES OF
 *    ANY KIND, INCLUDING BUT NOT LIMITED TO, LOST PROFITS OR ANY CONSEQUENTIAL,
 *    SPECIAL, INCIDENTAL, INDIRECT, OR DIRECT DAMAGES, HOWEVER CAUSED AND ON ANY
 *    THEORY OF LIABILITY, ARISING OUT OF THIS AGREEMENT.  THE FOREGOING SHALL
 *    APPLY TO THE EXTENT PERMITTED BY APPLICABLE LAW.
 *
 *    6.GENERAL.
 *
 *       6.1 Governing Law. This Agreement will be governed by and interpreted in
 *       accordance with the laws of the state of California, without reference to
 *       its conflict of laws principles.  If Licensee is located within the
 *       United States, all disputes arising out of this Agreement are subject to
 *       the exclusive jurisdiction of courts located in Santa Clara County,
 *       California. USA.  If Licensee is located outside of the United States,
 *       any dispute, controversy or claim arising out of or relating to this
 *       Agreement will be referred to and finally determined by arbitration in
 *       accordance with the JAMS International Arbitration Rules.  The tribunal
 *       will consist of one arbitrator.  The place of arbitration will be Palo
 *       Alto, California. The language to be used in the arbitral proceedings
 *       will be English.  Judgment upon the award rendered by the arbitrator may
 *       be entered in any court having jurisdiction thereof.
 *
 *       6.2 Assignment.  Licensee is not authorized to assign its rights under
 *       this Agreement to any third party. Confluent may freely assign its rights
 *       under this Agreement to any third party.
 *
 *       6.3 Other.  This Agreement is the entire agreement between the parties
 *       regarding the subject matter hereof.  No amendment or modification of
 *       this Agreement will be valid or binding upon the parties unless made in
 *       writing and signed by the duly authorized representatives of both
 *       parties.  In the event that any provision, including without limitation
 *       any condition, of this Agreement is held to be unenforceable, this
 *       Agreement and all licenses and rights granted hereunder will immediately
 *       terminate.  Waiver by Confluent of a breach of any provision of this
 *       Agreement or the failure by Confluent to exercise any right hereunder
 *       will not be construed as a waiver of any subsequent breach of that right
 *       or as a waiver of any other right.
 */
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
