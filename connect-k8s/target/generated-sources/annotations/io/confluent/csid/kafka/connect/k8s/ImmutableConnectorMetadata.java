package io.confluent.csid.kafka.connect.k8s;

import io.kubernetes.client.openapi.models.V1ObjectMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link ConnectorMetadata}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableConnectorMetadata.builder()}.
 */
@Generated(from = "ConnectorMetadata", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableConnectorMetadata implements ConnectorMetadata {
  private transient final Map<String, String> labels;
  private final String cluster;
  private final int taskCount;
  private final String namespace;
  private final String connectorName;
  private transient final V1ObjectMeta configMetaTask;
  private transient final V1ObjectMeta configMetaConnector;

  private ImmutableConnectorMetadata(String cluster, int taskCount, String namespace, String connectorName) {
    this.cluster = cluster;
    this.taskCount = taskCount;
    this.namespace = namespace;
    this.connectorName = connectorName;
    this.labels = initShim.labels();
    this.configMetaTask = initShim.configMetaTask();
    this.configMetaConnector = initShim.configMetaConnector();
    this.initShim = null;
  }

  private static final byte STAGE_INITIALIZING = -1;
  private static final byte STAGE_UNINITIALIZED = 0;
  private static final byte STAGE_INITIALIZED = 1;
  private transient volatile InitShim initShim = new InitShim();

  @Generated(from = "ConnectorMetadata", generator = "Immutables")
  private final class InitShim {
    private byte labelsBuildStage = STAGE_UNINITIALIZED;
    private Map<String, String> labels;

    Map<String, String> labels() {
      if (labelsBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (labelsBuildStage == STAGE_UNINITIALIZED) {
        labelsBuildStage = STAGE_INITIALIZING;
        this.labels = Objects.requireNonNull(labelsInitialize(), "labels");
        labelsBuildStage = STAGE_INITIALIZED;
      }
      return this.labels;
    }

    private byte configMetaTaskBuildStage = STAGE_UNINITIALIZED;
    private V1ObjectMeta configMetaTask;

    V1ObjectMeta configMetaTask() {
      if (configMetaTaskBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (configMetaTaskBuildStage == STAGE_UNINITIALIZED) {
        configMetaTaskBuildStage = STAGE_INITIALIZING;
        this.configMetaTask = Objects.requireNonNull(configMetaTaskInitialize(), "configMetaTask");
        configMetaTaskBuildStage = STAGE_INITIALIZED;
      }
      return this.configMetaTask;
    }

    private byte configMetaConnectorBuildStage = STAGE_UNINITIALIZED;
    private V1ObjectMeta configMetaConnector;

    V1ObjectMeta configMetaConnector() {
      if (configMetaConnectorBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (configMetaConnectorBuildStage == STAGE_UNINITIALIZED) {
        configMetaConnectorBuildStage = STAGE_INITIALIZING;
        this.configMetaConnector = Objects.requireNonNull(configMetaConnectorInitialize(), "configMetaConnector");
        configMetaConnectorBuildStage = STAGE_INITIALIZED;
      }
      return this.configMetaConnector;
    }

    private String formatInitCycleMessage() {
      List<String> attributes = new ArrayList<>();
      if (labelsBuildStage == STAGE_INITIALIZING) attributes.add("labels");
      if (configMetaTaskBuildStage == STAGE_INITIALIZING) attributes.add("configMetaTask");
      if (configMetaConnectorBuildStage == STAGE_INITIALIZING) attributes.add("configMetaConnector");
      return "Cannot build ConnectorMetadata, attribute initializers form cycle " + attributes;
    }
  }

  private Map<String, String> labelsInitialize() {
    return ConnectorMetadata.super.labels();
  }

  private V1ObjectMeta configMetaTaskInitialize() {
    return ConnectorMetadata.super.configMetaTask();
  }

  private V1ObjectMeta configMetaConnectorInitialize() {
    return ConnectorMetadata.super.configMetaConnector();
  }

  /**
   * @return The computed-at-construction value of the {@code labels} attribute
   */
  @Override
  public Map<String, String> labels() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.labels()
        : this.labels;
  }

  /**
   * @return The value of the {@code cluster} attribute
   */
  @Override
  public String cluster() {
    return cluster;
  }

  /**
   * @return The value of the {@code taskCount} attribute
   */
  @Override
  public int taskCount() {
    return taskCount;
  }

  /**
   * @return The value of the {@code namespace} attribute
   */
  @Override
  public String namespace() {
    return namespace;
  }

  /**
   * @return The value of the {@code connectorName} attribute
   */
  @Override
  public String connectorName() {
    return connectorName;
  }

  /**
   * @return The computed-at-construction value of the {@code configMetaTask} attribute
   */
  @Override
  public V1ObjectMeta configMetaTask() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.configMetaTask()
        : this.configMetaTask;
  }

  /**
   * @return The computed-at-construction value of the {@code configMetaConnector} attribute
   */
  @Override
  public V1ObjectMeta configMetaConnector() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.configMetaConnector()
        : this.configMetaConnector;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ConnectorMetadata#cluster() cluster} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for cluster
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableConnectorMetadata withCluster(String value) {
    String newValue = Objects.requireNonNull(value, "cluster");
    if (this.cluster.equals(newValue)) return this;
    return new ImmutableConnectorMetadata(newValue, this.taskCount, this.namespace, this.connectorName);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ConnectorMetadata#taskCount() taskCount} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for taskCount
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableConnectorMetadata withTaskCount(int value) {
    if (this.taskCount == value) return this;
    return new ImmutableConnectorMetadata(this.cluster, value, this.namespace, this.connectorName);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ConnectorMetadata#namespace() namespace} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for namespace
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableConnectorMetadata withNamespace(String value) {
    String newValue = Objects.requireNonNull(value, "namespace");
    if (this.namespace.equals(newValue)) return this;
    return new ImmutableConnectorMetadata(this.cluster, this.taskCount, newValue, this.connectorName);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ConnectorMetadata#connectorName() connectorName} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for connectorName
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableConnectorMetadata withConnectorName(String value) {
    String newValue = Objects.requireNonNull(value, "connectorName");
    if (this.connectorName.equals(newValue)) return this;
    return new ImmutableConnectorMetadata(this.cluster, this.taskCount, this.namespace, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableConnectorMetadata} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableConnectorMetadata
        && equalTo((ImmutableConnectorMetadata) another);
  }

  private boolean equalTo(ImmutableConnectorMetadata another) {
    return labels.equals(another.labels)
        && cluster.equals(another.cluster)
        && taskCount == another.taskCount
        && namespace.equals(another.namespace)
        && connectorName.equals(another.connectorName)
        && configMetaTask.equals(another.configMetaTask)
        && configMetaConnector.equals(another.configMetaConnector);
  }

  /**
   * Computes a hash code from attributes: {@code labels}, {@code cluster}, {@code taskCount}, {@code namespace}, {@code connectorName}, {@code configMetaTask}, {@code configMetaConnector}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + labels.hashCode();
    h += (h << 5) + cluster.hashCode();
    h += (h << 5) + taskCount;
    h += (h << 5) + namespace.hashCode();
    h += (h << 5) + connectorName.hashCode();
    h += (h << 5) + configMetaTask.hashCode();
    h += (h << 5) + configMetaConnector.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code ConnectorMetadata} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "ConnectorMetadata{"
        + "labels=" + labels
        + ", cluster=" + cluster
        + ", taskCount=" + taskCount
        + ", namespace=" + namespace
        + ", connectorName=" + connectorName
        + ", configMetaTask=" + configMetaTask
        + ", configMetaConnector=" + configMetaConnector
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link ConnectorMetadata} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable ConnectorMetadata instance
   */
  public static ImmutableConnectorMetadata copyOf(ConnectorMetadata instance) {
    if (instance instanceof ImmutableConnectorMetadata) {
      return (ImmutableConnectorMetadata) instance;
    }
    return ImmutableConnectorMetadata.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableConnectorMetadata ImmutableConnectorMetadata}.
   * <pre>
   * ImmutableConnectorMetadata.builder()
   *    .cluster(String) // required {@link ConnectorMetadata#cluster() cluster}
   *    .taskCount(int) // required {@link ConnectorMetadata#taskCount() taskCount}
   *    .namespace(String) // required {@link ConnectorMetadata#namespace() namespace}
   *    .connectorName(String) // required {@link ConnectorMetadata#connectorName() connectorName}
   *    .build();
   * </pre>
   * @return A new ImmutableConnectorMetadata builder
   */
  public static ImmutableConnectorMetadata.Builder builder() {
    return new ImmutableConnectorMetadata.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableConnectorMetadata ImmutableConnectorMetadata}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "ConnectorMetadata", generator = "Immutables")
  public static final class Builder {
    private static final long INIT_BIT_CLUSTER = 0x1L;
    private static final long INIT_BIT_TASK_COUNT = 0x2L;
    private static final long INIT_BIT_NAMESPACE = 0x4L;
    private static final long INIT_BIT_CONNECTOR_NAME = 0x8L;
    private long initBits = 0xfL;

    private String cluster;
    private int taskCount;
    private String namespace;
    private String connectorName;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code ConnectorMetadata} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(ConnectorMetadata instance) {
      Objects.requireNonNull(instance, "instance");
      cluster(instance.cluster());
      taskCount(instance.taskCount());
      namespace(instance.namespace());
      connectorName(instance.connectorName());
      return this;
    }

    /**
     * Initializes the value for the {@link ConnectorMetadata#cluster() cluster} attribute.
     * @param cluster The value for cluster 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder cluster(String cluster) {
      this.cluster = Objects.requireNonNull(cluster, "cluster");
      initBits &= ~INIT_BIT_CLUSTER;
      return this;
    }

    /**
     * Initializes the value for the {@link ConnectorMetadata#taskCount() taskCount} attribute.
     * @param taskCount The value for taskCount 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder taskCount(int taskCount) {
      this.taskCount = taskCount;
      initBits &= ~INIT_BIT_TASK_COUNT;
      return this;
    }

    /**
     * Initializes the value for the {@link ConnectorMetadata#namespace() namespace} attribute.
     * @param namespace The value for namespace 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder namespace(String namespace) {
      this.namespace = Objects.requireNonNull(namespace, "namespace");
      initBits &= ~INIT_BIT_NAMESPACE;
      return this;
    }

    /**
     * Initializes the value for the {@link ConnectorMetadata#connectorName() connectorName} attribute.
     * @param connectorName The value for connectorName 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder connectorName(String connectorName) {
      this.connectorName = Objects.requireNonNull(connectorName, "connectorName");
      initBits &= ~INIT_BIT_CONNECTOR_NAME;
      return this;
    }

    /**
     * Builds a new {@link ImmutableConnectorMetadata ImmutableConnectorMetadata}.
     * @return An immutable instance of ConnectorMetadata
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableConnectorMetadata build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableConnectorMetadata(cluster, taskCount, namespace, connectorName);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_CLUSTER) != 0) attributes.add("cluster");
      if ((initBits & INIT_BIT_TASK_COUNT) != 0) attributes.add("taskCount");
      if ((initBits & INIT_BIT_NAMESPACE) != 0) attributes.add("namespace");
      if ((initBits & INIT_BIT_CONNECTOR_NAME) != 0) attributes.add("connectorName");
      return "Cannot build ConnectorMetadata, some of required attributes are not set " + attributes;
    }
  }
}
