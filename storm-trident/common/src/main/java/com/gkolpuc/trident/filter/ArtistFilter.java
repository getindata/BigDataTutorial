package com.gkolpuc.trident.filter;

import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

import com.gkolpuc.trident.enums.Artist;

public class ArtistFilter extends BaseFilter {

	private static final long serialVersionUID = 8087773064991117893L;
	private Artist artist;

	public ArtistFilter(Artist artist) {
		this.artist = artist;
	}

	@Override
	public boolean isKeep(TridentTuple tuple) {
		Integer artistId = tuple.getIntegerByField("artistId");
		// System.out.println(artistId + "!=" + artist.getId() + " : " +
		// isValid(artistId));
		return isValid(artistId);
	}

	private boolean isValid(Integer artistId) {
		return !artistId.equals(artist.getId());
	}

}
