package com.gkolpuc.trident.exc2;

import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

import com.gkolpuc.trident.enums.Artist;
import com.gkolpuc.trident.enums.Song;
import com.gkolpuc.trident.filter.ArtistFilter;
import com.gkolpuc.trident.function.GlobalCountFunction;
import com.gkolpuc.trident.function.StarFunction;

public class StarAssignmentTopology {

	private StormTopology createTopology() {
		TridentTopology tridentTopology = new TridentTopology();

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("id", "songName", "artistId", "LocationCode",
				"timestamp"), 30, Song.testData(20000));

		tridentTopology.newStream("randomSongsSpout", spout)
				.each(new Fields("artistId"), new ArtistFilter(Artist.WhitneyHouston))
				.each(new Fields("id", "songName"), new GlobalCountFunction(), new Fields("count"))
				.each(new Fields("id", "songName", "count"), new StarFunction(), new Fields("stars"));
		return tridentTopology.build();

	}

	public static void main(String[] args) throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("song-counting", new Config(), new StarAssignmentTopology().createTopology());
		// Thread.sleep(15 * 1000);
		// cluster.shutdown();
	}
}
