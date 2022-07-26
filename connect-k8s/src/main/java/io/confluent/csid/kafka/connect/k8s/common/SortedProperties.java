package io.confluent.csid.kafka.connect.k8s.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SortedProperties extends Properties {
  public SortedProperties(Map<String, String> map) {
    this();
    this.putAll(map);
  }

  public SortedProperties() {
    super();
  }

  @Override
  public Set<Map.Entry<Object, Object>> entrySet() {
    Set<Map.Entry<Object, Object>> result = new LinkedHashSet<>();
    super.entrySet().stream().sorted((o1, o2) -> {
      String keyA = o1.toString();
      String keyB = o2.toString();
      return keyA.compareTo(keyB);
    }).forEach(result::add);
    return result;
  }


  @Override
  public void store(Writer writer, String comments) throws IOException {
    BufferedWriter bufferedWriter = writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);

    try (StringWriter stringWriter = new StringWriter()) {
      super.store(stringWriter, comments);
      String contents = stringWriter.toString();
      try (StringReader reader = new StringReader(contents)) {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
          String line;
          while (null != (line = bufferedReader.readLine())) {
            if (!line.startsWith("#")) {
              bufferedWriter.write(line);
              bufferedWriter.newLine();
            }
          }
        }
      }
    }
    bufferedWriter.flush();
  }

  @Override
  public void store(OutputStream out, String comments) throws IOException {
    OutputStreamWriter writer = new OutputStreamWriter(out);
    store(writer, comments);
    writer.flush();
  }

  @Override
  public synchronized String toString() {
    try (StringWriter writer = new StringWriter()) {
      store(writer, null);
      return writer.toString();
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
