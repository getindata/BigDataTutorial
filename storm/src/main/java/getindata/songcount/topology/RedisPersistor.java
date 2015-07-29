package getindata.songcount.topology;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.Config;
import backtype.storm.Constants;


import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisPersistor extends BaseBasicBolt {
	private final Logger logger = LoggerFactory.getLogger(RedisPersistor.class);

	private String username;
	private Jedis jedis;
	private ObjectMapper objectMapper;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		String redis = stormConf.containsKey("redis") ? (String) stormConf
				.get("redis") : "localhost";
		jedis = new Jedis(redis);
		objectMapper = new ObjectMapper();
		username = (String) stormConf.get("username");
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		if (isTickTuple(tuple)) {
			jedis.flushAll();
		} else {
		Long songId = tuple.getLongByField("songId");
		String key = username + "-" + songId;
		try {
			jedis.incr(key);
		} catch (Exception e) {
			logger.error("Error persisting for key: " + key, e);
		}
	}
}

	@Override
	public void cleanup() {
		if (jedis.isConnected()) {
			jedis.quit();
		}
	}

  @Override
  public Map<String, Object> getComponentConfiguration() {
    Config conf = new Config();
    conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
    return conf;
  }

 private boolean isTickTuple(Tuple tuple) {
    String sourceComponent = tuple.getSourceComponent();
    String sourceStreamId = tuple.getSourceStreamId();
    return sourceComponent.equals(Constants.SYSTEM_COMPONENT_ID)
        && sourceStreamId.equals(Constants.SYSTEM_TICK_STREAM_ID);
  }

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// No output fields to be declared
	}
}
