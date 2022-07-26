package io.confluent.csid.kafka.connect.k8s.hearder;

import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.runtime.WorkerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

public class HearderMain {
  private static final Logger log = LoggerFactory.getLogger(HearderMain.class);

  public static void main(String[] args) throws Exception {
    if (args.length != 1 || Arrays.asList(args).contains("--help")) {
      log.info("Usage: HearderMain worker.properties");
      System.exit(1);
    }

    String workerPropsFile = args[0];
    Map<String, String> workerProps = Utils.propsToStringMap(Utils.loadProps(workerPropsFile));

    WorkerInfo workerInfo = new WorkerInfo();
    workerInfo.logAll();

    HearderConfig config = new HearderConfig(workerProps);

  }
}
