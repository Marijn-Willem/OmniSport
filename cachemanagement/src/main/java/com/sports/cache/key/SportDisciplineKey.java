package com.sports.cache.key;

import com.sports.logic.util.Util;

public class SportDisciplineKey extends CacheKey {
	private final int sportId;
	private final int sportDisciplineId;

	public SportDisciplineKey(int sportId, int sportDisciplineId) {
		this.sportId = sportId;
		this.sportDisciplineId = sportDisciplineId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStringsWithDelimiter(Integer.toString(sportId), Integer.toString(sportDisciplineId), "|");
	}
}
