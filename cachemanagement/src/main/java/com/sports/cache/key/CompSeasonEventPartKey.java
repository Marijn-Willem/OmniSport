package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonEventPartKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final int compSeasonEventPartId;

	public CompSeasonEventPartKey(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.compSeasonEventPartId = compSeasonEventPartId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(compSeasonEventId), Integer.toString(compSeasonEventPartId)
		}, "|");
	}
}
