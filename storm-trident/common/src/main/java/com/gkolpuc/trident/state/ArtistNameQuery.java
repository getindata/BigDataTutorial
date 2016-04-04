package com.gkolpuc.trident.state;

import java.util.List;
import java.util.Map;

import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.state.QueryFunction;
import storm.trident.state.State;
import storm.trident.tuple.TridentTuple;

public class ArtistNameQuery implements QueryFunction {

	@Override
	public void prepare(Map conf, TridentOperationContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public List batchRetrieve(State state, List args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(TridentTuple tuple, Object result, TridentCollector collector) {
		// TODO Auto-generated method stub

	}

}
