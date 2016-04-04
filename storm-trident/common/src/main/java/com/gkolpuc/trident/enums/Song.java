package com.gkolpuc.trident.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import backtype.storm.tuple.Values;

public enum Song {
	Thiller(1, "Thiller", Artist.MichaelJackson), BillyJean(2, "Billy Jean", Artist.MichaelJackson), BeatIt(3,
			"BeatIt", Artist.MichaelJackson), IWillAlwaysLoveYou(4, "I Will Always Love You", Artist.WhitneyHouston),

	;

	Integer id;
	String name;
	Artist artist;

	private Song(Integer id, String name, Artist artist) {
		this.id = id;
		this.name = name;
		this.artist = artist;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Artist getArtist() {
		return artist;
	}

	public static Song randomSong() {
		Integer songIndex = Math.abs(new Random().nextInt()) % values().length;
		return values()[songIndex];
	}

	public static Values[] testData(Integer size) {
		List<Values> testData = new ArrayList<Values>(size);

		for (int i = 1; i <= size; i++) {
			Song song = Song.randomSong();

			Values e = new Values(song.getId(), song.getName(), song.getArtist().getId(),
					LocationCode.randomLocation(), System.currentTimeMillis());
			testData.add(e);
			// System.out.println(e);
		}
		return testData.toArray(new Values[size]);
	}

}
