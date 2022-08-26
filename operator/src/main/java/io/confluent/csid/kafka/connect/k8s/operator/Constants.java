package io.confluent.csid.kafka.connect.k8s.operator;


public class Constants {
  public static String getServiceAccountName() {
    return System.getProperty("operator.service.account.name");
  }

}
