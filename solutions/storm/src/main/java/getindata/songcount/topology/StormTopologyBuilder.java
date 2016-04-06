package getindata.songcount.topology;

import backtype.storm.Config;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;

public class StormTopologyBuilder {
	public static final String USER_NAME = System.getProperty("user.name");
	public static final String TOPOLOGY_NAME = USER_NAME + "-realtime-song-count";
	public static final String SERVER_LOGS_ID = "server-logs";
	public static final String SONG_PLAYED_FILTER_ID = "song-played-filter";
	public static final String PERSISTOR_ID = "persistor";

	public static StormTopology build() {

		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setSpout(SERVER_LOGS_ID, new ServerlLogs());
		builder.setBolt(SONG_PLAYED_FILTER_ID, new SongPlayedFilter())
				.shuffleGrouping(SERVER_LOGS_ID);
		builder.setBolt(PERSISTOR_ID, new RedisPersistor()).fieldsGrouping(
				SONG_PLAYED_FILTER_ID, new Fields("songId"));

		return builder.createTopology();
	}
}