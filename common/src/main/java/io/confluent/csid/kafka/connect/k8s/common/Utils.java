package io.confluent.csid.kafka.connect.k8s.common;

import java.util.Collection;
import java.util.function.Consumer;

public class Utils {
  public static <T> void ifPresent(T value, Consumer<T> consumer) {
    if (null != value) {
      consumer.accept(value);
    }
  }

  public static <T> void ifNotEmpty(Collection<T> value, Consumer<Collection<T>> consumer) {
    if (null != value && !value.isEmpty()) {
      consumer.accept(value);
    }
  }
}
