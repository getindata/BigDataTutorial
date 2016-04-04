package com.gkolpuc.trident.function;

import java.util.Arrays;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class StarFunction extends BaseFunction {

	private static final long serialVersionUID = -1142363311173425001L;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {

		String songName = tuple.getStringByField("songName");
		Integer count = tuple.getInteger(2);
		System.out.println(songName + " " + getStars(count) + "[count=" + count + "]");
		collector.emit(Arrays.asList(getStars(count)));
	}

	private boolean hasNewStar(Integer count) {
		if (count.equals(10))
			return true;
		if (count.equals(100))
			return true;
		if (count.equals(1000))
			return true;
		if (count.equals(10000))
			return true;
		if (count.equals(100000))
			return true;
		return false;
	}

	Object getStars(Integer count) {
		if (count >= 100000)
			return "*****";
		if (count >= 10000)
			return "****";
		if (count >= 1000)
			return "***";
		if (count >= 100)
			return "**";
		if (count >= 10)
			return "*";
		return null;
	}
}
