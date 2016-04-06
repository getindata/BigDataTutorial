package getindata.songcount.topology;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

public class SongPlayedFilter extends BaseBasicBolt {

  @Override
  public void declareOutputFields(OutputFieldsDeclarer fieldsDeclarer) {
    // ********************
    // IMPLEMENT YOUR CODE HERE
    // ********************
  }

  @Override
  public void execute(Tuple tuple,
                      BasicOutputCollector outputCollector) {
    String eventType = tuple.getStringByField("eventType");
    if (eventType.equals("SongPlayed")) {
      // ********************
      // IMPLEMENT YOUR CODE HERE
      // ********************
    }
  }
}
