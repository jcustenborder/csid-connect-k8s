package io.confluent.csid.kafka.connect.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link K8sClient}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableK8sClient.builder()}.
 */
@Generated(from = "K8sClient", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableK8sClient implements K8sClient {
  private final ApiClient client;
  private transient final CoreV1Api coreV1Api;
  private transient final AppsV1Api appsV1Api;

  private ImmutableK8sClient(ApiClient client) {
    this.client = client;
    this.coreV1Api = initShim.coreV1Api();
    this.appsV1Api = initShim.appsV1Api();
    this.initShim = null;
  }

  private static final byte STAGE_INITIALIZING = -1;
  private static final byte STAGE_UNINITIALIZED = 0;
  private static final byte STAGE_INITIALIZED = 1;
  private transient volatile InitShim initShim = new InitShim();

  @Generated(from = "K8sClient", generator = "Immutables")
  private final class InitShim {
    private byte coreV1ApiBuildStage = STAGE_UNINITIALIZED;
    private CoreV1Api coreV1Api;

    CoreV1Api coreV1Api() {
      if (coreV1ApiBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (coreV1ApiBuildStage == STAGE_UNINITIALIZED) {
        coreV1ApiBuildStage = STAGE_INITIALIZING;
        this.coreV1Api = Objects.requireNonNull(coreV1ApiInitialize(), "coreV1Api");
        coreV1ApiBuildStage = STAGE_INITIALIZED;
      }
      return this.coreV1Api;
    }

    private byte appsV1ApiBuildStage = STAGE_UNINITIALIZED;
    private AppsV1Api appsV1Api;

    AppsV1Api appsV1Api() {
      if (appsV1ApiBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (appsV1ApiBuildStage == STAGE_UNINITIALIZED) {
        appsV1ApiBuildStage = STAGE_INITIALIZING;
        this.appsV1Api = Objects.requireNonNull(appsV1ApiInitialize(), "appsV1Api");
        appsV1ApiBuildStage = STAGE_INITIALIZED;
      }
      return this.appsV1Api;
    }

    private String formatInitCycleMessage() {
      List<String> attributes = new ArrayList<>();
      if (coreV1ApiBuildStage == STAGE_INITIALIZING) attributes.add("coreV1Api");
      if (appsV1ApiBuildStage == STAGE_INITIALIZING) attributes.add("appsV1Api");
      return "Cannot build K8sClient, attribute initializers form cycle " + attributes;
    }
  }

  private CoreV1Api coreV1ApiInitialize() {
    return K8sClient.super.coreV1Api();
  }

  private AppsV1Api appsV1ApiInitialize() {
    return K8sClient.super.appsV1Api();
  }

  /**
   * @return The value of the {@code client} attribute
   */
  @Override
  public ApiClient client() {
    return client;
  }

  /**
   * @return The computed-at-construction value of the {@code coreV1Api} attribute
   */
  @Override
  public CoreV1Api coreV1Api() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.coreV1Api()
        : this.coreV1Api;
  }

  /**
   * @return The computed-at-construction value of the {@code appsV1Api} attribute
   */
  @Override
  public AppsV1Api appsV1Api() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.appsV1Api()
        : this.appsV1Api;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link K8sClient#client() client} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for client
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableK8sClient withClient(ApiClient value) {
    if (this.client == value) return this;
    ApiClient newValue = Objects.requireNonNull(value, "client");
    return new ImmutableK8sClient(newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableK8sClient} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableK8sClient
        && equalTo((ImmutableK8sClient) another);
  }

  private boolean equalTo(ImmutableK8sClient another) {
    return client.equals(another.client)
        && coreV1Api.equals(another.coreV1Api)
        && appsV1Api.equals(another.appsV1Api);
  }

  /**
   * Computes a hash code from attributes: {@code client}, {@code coreV1Api}, {@code appsV1Api}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + client.hashCode();
    h += (h << 5) + coreV1Api.hashCode();
    h += (h << 5) + appsV1Api.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code K8sClient} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "K8sClient{"
        + "client=" + client
        + ", coreV1Api=" + coreV1Api
        + ", appsV1Api=" + appsV1Api
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link K8sClient} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable K8sClient instance
   */
  public static ImmutableK8sClient copyOf(K8sClient instance) {
    if (instance instanceof ImmutableK8sClient) {
      return (ImmutableK8sClient) instance;
    }
    return ImmutableK8sClient.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableK8sClient ImmutableK8sClient}.
   * <pre>
   * ImmutableK8sClient.builder()
   *    .client(io.kubernetes.client.openapi.ApiClient) // required {@link K8sClient#client() client}
   *    .build();
   * </pre>
   * @return A new ImmutableK8sClient builder
   */
  public static ImmutableK8sClient.Builder builder() {
    return new ImmutableK8sClient.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableK8sClient ImmutableK8sClient}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "K8sClient", generator = "Immutables")
  public static final class Builder {
    private static final long INIT_BIT_CLIENT = 0x1L;
    private long initBits = 0x1L;

    private ApiClient client;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code K8sClient} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(K8sClient instance) {
      Objects.requireNonNull(instance, "instance");
      client(instance.client());
      return this;
    }

    /**
     * Initializes the value for the {@link K8sClient#client() client} attribute.
     * @param client The value for client 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder client(ApiClient client) {
      this.client = Objects.requireNonNull(client, "client");
      initBits &= ~INIT_BIT_CLIENT;
      return this;
    }

    /**
     * Builds a new {@link ImmutableK8sClient ImmutableK8sClient}.
     * @return An immutable instance of K8sClient
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableK8sClient build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableK8sClient(client);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_CLIENT) != 0) attributes.add("client");
      return "Cannot build K8sClient, some of required attributes are not set " + attributes;
    }
  }
}
