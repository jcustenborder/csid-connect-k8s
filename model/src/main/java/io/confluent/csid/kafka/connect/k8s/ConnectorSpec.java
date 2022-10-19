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
package io.confluent.csid.kafka.connect.k8s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.fabric8.kubernetes.api.model.LocalObjectReference;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class ConnectorSpec {

  @NotNull
  @JsonProperty("connector")
  @JsonPropertyDescription("Configuration for the connector container")
  private ContainerConfig connector;


  @NotNull
  @JsonProperty("task")
  @JsonPropertyDescription("Configuration for the task container(s).")
  private ContainerConfig task;


  @NotNull
  @JsonProperty("connectorConfig")
  @JsonPropertyDescription("The configuration for the connector")
  private Map<String, String> connectorConfig = new LinkedHashMap<>();


  private List<LocalObjectReference> imagePullSecrets = new ArrayList<>();

  @JsonProperty("loggingConfig")
  @JsonPropertyDescription("Logging configuration for the containers.")
  private LoggingConfig loggingConfig = new LoggingConfig();

  @JsonProperty("downloadPlugins")
  @JsonPropertyDescription("List of plugins to download from the Confluent hub. ex 'confluentinc/kafka-connect-datagen:0.5.3'")
  private List<String> downloadPlugins = new ArrayList<>();

  @JsonProperty("workerConfigSecret")
  @JsonPropertyDescription("Secret in the local namespace that holds the worker.properties configuration file.")
  private LocalObjectReference workerConfigSecret;

  private String imagePullPolicy;



  public ContainerConfig getConnector() {
    return connector;
  }

  public void setConnector(ContainerConfig connector) {
    this.connector = connector;
  }

  public ContainerConfig getTask() {
    return task;
  }

  public void setTask(ContainerConfig task) {
    this.task = task;
  }

  public Map<String, String> getConnectorConfig() {
    return connectorConfig;
  }

  public void setConnectorConfig(Map<String, String> connectorConfig) {
    this.connectorConfig = connectorConfig;
  }

  public List<LocalObjectReference> getImagePullSecrets() {
    return imagePullSecrets;
  }

  public void setImagePullSecrets(List<LocalObjectReference> imagePullSecrets) {
    this.imagePullSecrets = imagePullSecrets;
  }

  public LoggingConfig getLoggingConfig() {
    return loggingConfig;
  }

  public void setLoggingConfig(LoggingConfig loggingConfig) {
    this.loggingConfig = loggingConfig;
  }

  public List<String> getDownloadPlugins() {
    return downloadPlugins;
  }

  public void setDownloadPlugins(List<String> downloadPlugins) {
    this.downloadPlugins = downloadPlugins;
  }

  public LocalObjectReference getWorkerConfigSecret() {
    return workerConfigSecret;
  }

  public void setWorkerConfigSecret(LocalObjectReference workerConfigSecret) {
    this.workerConfigSecret = workerConfigSecret;
  }

  public String getImagePullPolicy() {
    return imagePullPolicy;
  }

  public void setImagePullPolicy(String imagePullPolicy) {
    this.imagePullPolicy = imagePullPolicy;
  }
}
