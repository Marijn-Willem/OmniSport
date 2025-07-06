package com.sports.cache.key;

import com.sports.logic.util.Util;

public class SportEventKey extends CacheFragmentKey {
	private final int sportId;
	private final int sportEventId;

	public SportEventKey(int sportId, int sportEventId) {
		this.sportId = sportId;
		this.sportEventId = sportEventId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStringsWithDelimiter(Integer.toString(sportId), Integer.toString(sportEventId), "|");
	}
}
