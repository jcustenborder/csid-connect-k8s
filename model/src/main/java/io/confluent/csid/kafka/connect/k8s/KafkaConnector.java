package io.confluent.csid.kafka.connect.k8s;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("io.confluent.csid")
@Version("v1")
public class KafkaConnector extends CustomResource<KafkaConnectorSpec, KafkaConnectorStatus> implements Namespaced {




}
