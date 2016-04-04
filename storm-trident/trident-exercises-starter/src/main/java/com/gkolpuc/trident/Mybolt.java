package com.gkolpuc.trident;

import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.metric.api.CountMetric;
import backtype.storm.metric.api.MeanReducer;
import backtype.storm.metric.api.MultiCountMetric;
import backtype.storm.metric.api.ReducedMetric;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * This is a basic example of a Storm topology.
 */
public class Mybolt {

	public static class ExclamationBolt extends BaseRichBolt {
		transient CountMetric _countMetric;
		transient MultiCountMetric _wordCountMetric;
		transient ReducedMetric _wordLengthMeanMetric;
		OutputCollector _collector;

		@Override
		public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
			_collector = collector;

			_countMetric = new CountMetric();
			_wordCountMetric = new MultiCountMetric();
			_wordLengthMeanMetric = new ReducedMetric(new MeanReducer());
			context.registerMetric("execute_count", _countMetric, 5);
			context.registerMetric("word_count", _wordCountMetric, 60);
			context.registerMetric("word_length", _wordLengthMeanMetric, 60);
		}

		@Override
		public void execute(Tuple tuple) {
			_collector.emit(tuple, new Values(tuple.getString(0) + "!!!"));
			_collector.ack(tuple);
			String word = "";

			_countMetric.incr();
			_wordCountMetric.scope(word).incr();
			_wordLengthMeanMetric.update(word.length());
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("word"));
		}

	}

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("word", new TestWordSpout(), 10);
		builder.setBolt("exclaim1", new ExclamationBolt(), 3).shuffleGrouping("word");
		builder.setBolt("exclaim2", new ExclamationBolt(), 2).shuffleGrouping("exclaim1");

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);

			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology("test");
			cluster.shutdown();
		}
	}
}