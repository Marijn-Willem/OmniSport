package com.sports.cache.key;

import com.sports.logic.util.Util;

public class H2HMatchKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int matchId;

	public H2HMatchKey(int competitionId, int seasonId, int matchId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.matchId = matchId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(matchId)
		}, "|");
	}
}
