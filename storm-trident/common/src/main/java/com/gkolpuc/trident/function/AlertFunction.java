package com.gkolpuc.trident.function;

import java.util.Arrays;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class AlertFunction extends BaseFunction {

	private static final long serialVersionUID = 6795448669850237014L;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Object x = tuple.get(0) + " - " + tuple.get(1) + " [ " + tuple.get(2) + " ]";
		System.out.println(x);
		collector.emit(Arrays.asList(x));
	}

}
