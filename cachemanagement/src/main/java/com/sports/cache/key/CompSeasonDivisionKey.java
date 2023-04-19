package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonDivisionKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int compDivisionId;

	public CompSeasonDivisionKey(int competitionId, int seasonId, int compDivisionId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compDivisionId = compDivisionId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(compDivisionId)
		}, "|");
	}
}
