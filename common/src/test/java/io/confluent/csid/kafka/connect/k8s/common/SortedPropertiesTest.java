package io.confluent.csid.kafka.connect.k8s.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortedPropertiesTest {

  @Test
  public void tostring() {
    SortedProperties properties = new SortedProperties();
    properties.put("zzz", "asdf");
    properties.put("aaaa", "1234123");
    properties.put("foo", "bar");

    String expected = "aaaa=1234123\n" +
        "foo=bar\n" +
        "zzz=asdf\n";

    String text = properties.toString();

    assertEquals(expected, text);
  }

}
