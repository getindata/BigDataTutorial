package getindata.songcount.topology;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class ServerlLogs extends BaseRichSpout {
  private List<String> logs;
  private int nextEmitIndex;
  private SpoutOutputCollector outputCollector;

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("eventType", "id"));
  }

  @Override
  public void open(Map map,
                   TopologyContext topologyContext,
                   SpoutOutputCollector spoutOutputCollector) {
    this.outputCollector = spoutOutputCollector;
    this.nextEmitIndex = 0;

    try {
      logs = IOUtils.readLines(ClassLoader.getSystemResourceAsStream("serverLogs.tsv"),
                                   Charset.defaultCharset().name());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void nextTuple() {
    String checkin = logs.get(nextEmitIndex);
    String[] parts = checkin.split("\t");
    String eventType = parts[3];
    Long id = Long.valueOf(parts[4]);
    outputCollector.emit(new Values(eventType, id));

    nextEmitIndex = (nextEmitIndex + 1) % logs.size();
  }
}
