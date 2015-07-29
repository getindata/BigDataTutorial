package getindata.songcount;

import getindata.songcount.topology.StormTopologyBuilder;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.utils.Utils;

public class LocalTopologyRunner {
  private static final int ONE_MINUTE = 60000;

  public static void main(String[] args) {
    Config config = new Config();
    config.setDebug(true);
    config.put("redis", "localhost");

    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology(StormTopologyBuilder.TOPOLOGY_NAME, config, StormTopologyBuilder.build());

    Utils.sleep(ONE_MINUTE);
    cluster.killTopology(StormTopologyBuilder.TOPOLOGY_NAME);
    cluster.shutdown();
  }
}