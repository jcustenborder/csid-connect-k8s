package io.confluent.csid.kafka.connect.k8s.common;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigHelperTest {

  @Test
  public void configDirSpecified() throws Exception {
    ConfigHelper helper = ConfigHelper.create("--config-dir", "/tmp", "--hostname", "testing-task-1");
    assertNotNull(helper);
  }

  @Test
  public void configDirNotSpecified() throws Exception {
    assertThrows(ArgumentParserException.class, ()->{
      ConfigHelper helper = ConfigHelper.create();
      assertNotNull(helper);
    });
  }

  @Test
  public void configDirNotFound() throws Exception {
    assertThrows(FileNotFoundException.class, ()->{
      ConfigHelper helper = ConfigHelper.create("--config-dir", "/tmp/not/there");
      assertNotNull(helper);
    });
  }

  @Test
  public void hostnameOverride() throws Exception {
    ConfigHelper helper = ConfigHelper.create("--hostname", "testing-task-1", "--config-dir", "/tmp");
    assertNotNull(helper);
    assertEquals("testing-task-1", helper.hostname);
    assertEquals("testing", helper.connectorName);
    assertEquals("task", helper.processType);
    assertEquals(Optional.of(1), helper.taskIndex);
  }

  @Test
  public void connectorHostname() throws Exception{
    ConfigHelper helper = ConfigHelper.create("--hostname", "jdbc-source-connector-connector-1", "--config-dir", "/tmp");
    assertNotNull(helper);
    assertEquals("jdbc-source-connector-connector-1", helper.hostname);
    assertEquals("jdbc-source-connector", helper.connectorName);
    assertEquals("connector", helper.processType);
    assertEquals(Optional.empty(), helper.taskIndex);
  }

  @Test
  public void taskHostname() throws Exception {
    ConfigHelper helper = ConfigHelper.create("--hostname", "jdbc-source-connector-task-1", "--config-dir", "/tmp");
    assertNotNull(helper);
  }

  @Test
  public void taskIndex() throws Exception {
    ConfigHelper helper = ConfigHelper.create("--hostname", "testing-task-1", "--config-dir", "/tmp");
    assertNotNull(helper);
    assertEquals(Optional.of(1), helper.taskIndex);
  }



}
