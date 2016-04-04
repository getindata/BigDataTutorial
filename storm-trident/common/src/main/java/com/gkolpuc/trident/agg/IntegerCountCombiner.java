package com.gkolpuc.trident.agg;

import storm.trident.operation.CombinerAggregator;
import storm.trident.tuple.TridentTuple;

public class IntegerCountCombiner implements CombinerAggregator<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2084934282112371761L;

	@Override
	public Integer init(TridentTuple tuple) {
		return 1;
	}

	@Override
	public Integer combine(Integer val1, Integer val2) {
		return val1 + val2;
	}

	@Override
	public Integer zero() {
		return 0;
	}

}
