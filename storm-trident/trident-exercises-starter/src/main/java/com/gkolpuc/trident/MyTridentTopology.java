package com.gkolpuc.trident;

import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

import com.gkolpuc.trident.enums.Song;

public class MyTridentTopology {

	private StormTopology createTopology() {
		TridentTopology tridentTopology = new TridentTopology();

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("id", "songName", "artistId", "LocationCode",
				"timestamp"), 10, Song.testData(20));

		tridentTopology.newStream("randomSongsSpout", spout);

		return tridentTopology.build();
	}

	public static void main(String[] args) throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("song-counting", new Config(), new MyTridentTopology().createTopology());
		Thread.sleep(15 * 1000);
		cluster.shutdown();
	}
}
