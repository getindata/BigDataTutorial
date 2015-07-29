package getindata.songcount;

import getindata.songcount.topology.StormTopologyBuilder;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;

public class RemoteTopologyRunner {

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.setDebug(true);
		
	    config.put("redis", args[0]);
	    
		config.setNumWorkers(2);
		StormSubmitter.submitTopologyWithProgressBar(
				StormTopologyBuilder.TOPOLOGY_NAME, config,
				StormTopologyBuilder.build());
	}
}