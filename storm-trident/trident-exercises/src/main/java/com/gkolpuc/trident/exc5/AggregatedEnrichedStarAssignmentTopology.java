package com.gkolpuc.trident.exc5;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.MapGet;
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
import com.gkolpuc.trident.function.AlertFunction;
import com.gkolpuc.trident.function.CachedStarFunction;
import com.gkolpuc.trident.state.StaticSingleKeyMapState;

public class AggregatedEnrichedStarAssignmentTopology {

	private StormTopology createTopology() {
		TridentTopology tridentTopology = new TridentTopology();

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("id", "songName", "artistId", "LocationCode",
				"timestamp"), 10000, Song.testData(400000));

		TridentState artists = tridentTopology.newStaticState(new StaticSingleKeyMapState.Factory(Artist.ARTISTS_DB));

		tridentTopology.newStream("randomSongsSpout", spout)
				.each(new Fields("artistId"), new ArtistFilter(Artist.WhitneyHouston))
				.groupBy(new Fields("id", "songName", "artistId"))
				.persistentAggregate(new MemoryMapState.Factory(), new IntegerCountCombiner(), new Fields("count"))
				.newValuesStream().partitionBy(new Fields("id"))
				.each(new Fields("id", "songName", "count"), new CachedStarFunction(), new Fields("stars"))
				.stateQuery(artists, new Fields("artistId"), new MapGet(), new Fields("artistName"))
				.each(new Fields("songName", "artistName", "stars"), new AlertFunction(), new Fields("alert"));
		return tridentTopology.build();
	}

	public static void main(String[] args) throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("song-counting", new Config(),
				new AggregatedEnrichedStarAssignmentTopology().createTopology());
		// Thread.sleep(15 * 1000);
		// cluster.shutdown();
	}
}
