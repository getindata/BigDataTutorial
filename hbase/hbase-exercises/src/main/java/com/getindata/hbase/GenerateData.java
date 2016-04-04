package com.getindata.hbase;

import java.util.Random;

public class GenerateData {

	public static void main(String[] args) {

		for (int i = 0; i <= 5000; i++) {

			Domain d = Domain.getrandom();
			FirstName f = FirstName.getrandom();
			City c = City.getrandom();
			int x = Math.abs(new Random().nextInt()) % 10000;
			System.out.println(f + ", " + c + ", " + f + x + "@" + d + ".com");
		}

	}

	enum Domain {
		gmail, hotmail, getindata, o2, wp, outlook, xyz, blabla, toobla, uk;

		public static Domain getrandom() {
			Domain[] values = values();
			int index = Math.abs(new Random().nextInt()) % values.length;
			return values[index];
		}
	}

	enum FirstName {
		Grzegorz, Adam, Krzysztof, Piotr, Tomasz, Jacek, Marta, Ania, Lukasz, Artur;
		public static FirstName getrandom() {
			FirstName[] values = values();
			int index = Math.abs(new Random().nextInt()) % values.length;
			return values[index];
		}
	}

	enum City {
		Gdansk, Warsaw, Wroclaw, Lublin, Poznan;
		public static City getrandom() {
			City[] values = values();
			int index = Math.abs(new Random().nextInt()) % values.length;
			return values[index];
		}
	}
}
