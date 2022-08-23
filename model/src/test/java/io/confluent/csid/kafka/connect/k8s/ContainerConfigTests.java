package io.confluent.csid.kafka.connect.k8s;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.QuantityBuilder;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainerConfigTests {

  ContainerConfig config;

  @BeforeEach
  public void setup() {
    this.config = new ContainerConfig();
  }

  @Test
  public void resourceRequirementsBothNull() {
    this.config.setLimits(null);
    this.config.setRequests(null);

    ResourceRequirements requirements = this.config.resourceRequirements();
    assertNull(requirements);
  }

  Map<String, Quantity> resourceOf(String key, Integer amount, String format) {
    return Map.of(
        key,
        new QuantityBuilder()
            .withAmount(Integer.toString(amount))
            .withFormat(format)
            .build()
    );
  }

  void assertLimit(Map<String, Quantity> actual, String key, Integer amount, String format) {
    assertTrue(actual.containsKey(key), String.format("actual should have key '%s'", key));
    Quantity actualQuantity = actual.get(key);
    Quantity expectedQuantity = new QuantityBuilder()
        .withAmount(Integer.toString(amount))
        .withFormat(format)
        .build();

    assertEquals(expectedQuantity, actualQuantity);
  }

  @Test
  public void resourceRequirementsBothSet() {

    this.config.setLimits(
        resourceOf("memory", 512, "M")
    );
    this.config.setRequests(
        resourceOf("memory", 384, "M")
    );
    ResourceRequirements requirements = this.config.resourceRequirements();
    assertNotNull(requirements);
    assertLimit(requirements.getLimits(), "memory", 512, "M");
    assertLimit(requirements.getRequests(), "memory", 384, "M");
  }

  @Test
  public void resourceRequirementsRequestsSet() {
    this.config.setRequests(
        resourceOf("memory", 512, "M")
    );

    ResourceRequirements requirements = this.config.resourceRequirements();
    assertNotNull(requirements);
    assertNull(requirements.getLimits());
    assertLimit(requirements.getRequests(), "memory", 512, "M");
  }

  @Test
  public void resourceRequirementsLimitsSet() {
    this.config.setLimits(
        resourceOf("memory", 512, "M")
    );

    ResourceRequirements requirements = this.config.resourceRequirements();
    assertNotNull(requirements);
    assertNull(requirements.getRequests());
    assertLimit(requirements.getLimits(), "memory", 512, "M");
  }

  @Test
  public void resourceRequirementsBlank() {
    this.config.setLimits(Map.of());
    ResourceRequirements requirements = this.config.resourceRequirements();
    assertNull(requirements);
  }

}
