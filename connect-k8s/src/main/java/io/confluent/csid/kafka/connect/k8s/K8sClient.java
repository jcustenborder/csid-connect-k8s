package io.confluent.csid.kafka.connect.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import org.immutables.value.Value;

@Value.Immutable
public interface K8sClient {
  ApiClient client();

  @Value.Derived
  default CoreV1Api coreV1Api() {
    return new CoreV1Api(client());
  }

  @Value.Derived
  default AppsV1Api appsV1Api() {
    return new AppsV1Api(client());
  }
}
