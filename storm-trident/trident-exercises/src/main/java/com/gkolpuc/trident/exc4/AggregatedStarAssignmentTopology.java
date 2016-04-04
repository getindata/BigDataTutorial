package com.gkolpuc.trident.exc4;

import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.testing.MemoryMapState;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

import com.gkolpuc.trident.agg.IntegerCountCombiner;
import com.gkolpuc.trident.enums.Artist;
import com.gkolpuc.trident.enums.Song;
import com.gkolpuc.trident.filter.ArtistFilter;
import com.gkolpuc.trident.function.StarFunction;

public class AggregatedStarAssignmentTopology {

	private StormTopology createTopology() {
		TridentTopology tridentTopology = new TridentTopology();

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("id", "songName", "artistId", "LocationCode",
				"timestamp"), 10, Song.testData(200));

		tridentTopology.newStream("randomSongsSpout", spout)
				.each(new Fields("artistId"), new ArtistFilter(Artist.WhitneyHouston))
				.groupBy(new Fields("id", "songName"))
				.persistentAggregate(new MemoryMapState.Factory(), new IntegerCountCombiner(), new Fields("count"))
				.newValuesStream().partitionBy(new Fields("id"))
				.each(new Fields("id", "songName", "count"), new StarFunction(), new Fields("stars"));
		return tridentTopology.build();
	}

	public static void main(String[] args) throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("song-counting", new Config(), new AggregatedStarAssignmentTopology().createTopology());
		// Thread.sleep(15 * 1000);
		// cluster.shutdown();
	}
}
