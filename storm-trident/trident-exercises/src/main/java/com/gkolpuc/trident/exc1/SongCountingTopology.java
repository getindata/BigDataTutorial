package com.gkolpuc.trident.exc1;

import com.gkolpuc.trident.enums.Song;
import com.gkolpuc.trident.function.GlobalCountFunction;

import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

public class SongCountingTopology {

	private StormTopology createTopology() {
		TridentTopology tridentTopology = new TridentTopology();

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("id", "songName", "artistId", "LocationCode",
				"timestamp"), 3, Song.testData(20));

		tridentTopology.newStream("randomSongsSpout", spout).each(new Fields("id", "songName"),
				new GlobalCountFunction(), new Fields("count"));

		return tridentTopology.build();
	}

	public static void main(String[] args) throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("song-counting", new Config(), new SongCountingTopology().createTopology());
		Thread.sleep(15 * 1000);
		cluster.shutdown();
	}
}
