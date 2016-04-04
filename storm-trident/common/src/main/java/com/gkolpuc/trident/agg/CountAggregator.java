package com.gkolpuc.trident.agg;

import storm.trident.operation.BaseAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

import com.gkolpuc.trident.agg.CountAggregator.CountState;

public class CountAggregator extends BaseAggregator<CountState> {

	private static final long serialVersionUID = -7857726455810852301L;

	static class CountState {
		Integer count = 0;
	}

	public CountState init(Object batchId, TridentCollector collector) {
		return new CountState();
	}

	public void aggregate(CountState state, TridentTuple tuple, TridentCollector collector) {
		state.count += 1;
	}

	public void complete(CountState state, TridentCollector collector) {
		collector.emit(new Values(state.count));
	}
}