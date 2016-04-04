package com.gkolpuc.trident.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.gkolpuc.trident.function.GlobalSumFunction.MutableInt;

import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class CachedStarFunction extends StarFunction {

	private static final long serialVersionUID = 197152079091549844L;
	Map<Integer, MutableString> starCache = new HashMap<Integer, MutableString>();

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Integer id = tuple.getIntegerByField("id");
		String songName = tuple.getStringByField("songName");
		Integer count = tuple.getInteger(2);
		Object stars = getStars(count);

		if (stars != null) {
			MutableString mutableString = starCache.get(id);

			if (mutableString == null) {
				starCache.put(id, new MutableString((String) stars));
				collector.emit(Arrays.asList(stars));
			} else {
				if (mutableString.receivedNewStar((String) stars)) {
					collector.emit(Arrays.asList(stars));
				}
			}
		}
	}

	class MutableString {
		String value = null;

		public MutableString(String initialValue) {
			this.value = initialValue;
		}

		public boolean receivedNewStar(String s) {
			if (!s.equals(value)) {
				value = s;
				return true;
			}
			return false;
		}

	}

}
