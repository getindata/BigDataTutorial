package com.gkolpuc.trident.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.Function;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

public class GlobalSumFunction extends BaseFunction {

	private static final long serialVersionUID = -2967258788045330326L;
	Map<Integer, MutableInt> counts = new HashMap<Integer, MutableInt>();

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Integer id = tuple.getIntegerByField("id");
		Integer batchCount = tuple.getInteger(2);

		MutableInt count = counts.get(id);
		if (count == null) {
			counts.put(id, new MutableInt(batchCount));
		} else {
			counts.get(id).incrementBy(batchCount);
		}
		String songName = tuple.getStringByField("songName");
		System.out.println("Current batchCount for " + songName + " : " + batchCount);
		System.out.println("Current GlobalCount for " + songName + " : " + counts.get(id).get());
		collector.emit(Arrays.asList(counts.get(id).getAsObject()));
	}

	class MutableInt {
		Integer value = 0;

		public MutableInt(Integer batchCount) {
			this.value = batchCount;
		}

		public void incrementBy(Integer x) {
			value += x;
		}

		public Integer get() {
			return value;
		}

		public Object getAsObject() {
			return value;
		}

	}
}
