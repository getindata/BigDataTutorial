package com.gkolpuc.trident.enums;

import java.util.Random;

public enum LocationCode {
	US, UK, PL;
	public static LocationCode randomLocation() {
		Integer songIndex = Math.abs(new Random().nextInt()) % values().length;
		return values()[songIndex];
	}

}
