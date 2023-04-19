package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonEventPartKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int sportEventId;
	private final int compSeasonEventPartId;

	public CompSeasonEventPartKey(int competitionId, int seasonId, int sportEventId, int compSeasonEventPartId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.sportEventId = sportEventId;
		this.compSeasonEventPartId = compSeasonEventPartId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(sportEventId), Integer.toString(compSeasonEventPartId)
		}, "|");
	}
}
