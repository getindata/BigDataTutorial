package com.gkolpuc.trident.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class GlobalCountFunction extends BaseFunction {

	private static final long serialVersionUID = -2967258788045330326L;
	Map<Integer, MutableInt> counts = new HashMap<Integer, MutableInt>();

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Integer id = tuple.getIntegerByField("id");

		MutableInt count = counts.get(id);
		if (count == null) {
			counts.put(id, new MutableInt());
		} else {
			counts.get(id).increment();
		}
		String songName = tuple.getStringByField("songName");
		// System.out.println("Current count for " + songName + " : " +
		// counts.get(id).get());
		collector.emit(Arrays.asList(counts.get(id).getAsObject()));
	}

	class MutableInt {
		Integer value = 1;

		public void increment() {
			++value;
		}

		public Integer get() {
			return value;
		}

		public Object getAsObject() {
			return value;
		}

	}
}
