package com.gkolpuc.trident.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Artist {
	MichaelJackson(1, "Michael Jackson"), CelineDion(2, "Celine Dion"), WhitneyHouston(3, "Whitney Houston")

	;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	Integer id;
	String name;

	private Artist(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Map<Integer, Artist> ARTISTS_DB = new HashMap<Integer, Artist>() {
		{
			for (Artist artist : Artist.values()) {
				put(artist.getId(), artist);
			}
		}
	};

}
