package io.confluent.csid.kafka.connect.k8s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ServiceSpec;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class ContainerConfig {

  @NotNull
  @JsonPropertyDescription("Docker image to use")
  @JsonProperty("image")
  private String image;

  @JsonPropertyDescription("Container specific resource requests")
  @JsonProperty("limits")
  private Map<String, Quantity> limits;
  @JsonPropertyDescription("Container specific resource limits")
  @JsonProperty("requests")
  private Map<String, Quantity> requests;


  @JsonProperty("serviceSpec")
  @JsonPropertyDescription("Configure a service")
  private ServiceSpec serviceSpec;

  public ServiceSpec getServiceSpec() {
    return serviceSpec;
  }

  public void setServiceSpec(ServiceSpec serviceSpec) {
    this.serviceSpec = serviceSpec;
  }

  static boolean isNull(Map<String, Quantity> containerResources) {
    return null == containerResources || containerResources.isEmpty();
  }


  public ResourceRequirements resourceRequirements() {
    if (isNull(this.limits) && isNull(this.requests)) {
      return null;
    }

    ResourceRequirements requirements = new ResourceRequirements();
    if (!isNull(this.limits)) {
      requirements.setLimits(this.limits);
    }
    if (!isNull(this.requests)) {
      requirements.setRequests(this.requests);
    }

    return requirements;
  }


  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Map<String, Quantity> getLimits() {
    return limits;
  }

  public void setLimits(Map<String, Quantity> limits) {
    this.limits = limits;
  }

  public Map<String, Quantity> getRequests() {
    return requests;
  }

  public void setRequests(Map<String, Quantity> requests) {
    this.requests = requests;
  }
}
