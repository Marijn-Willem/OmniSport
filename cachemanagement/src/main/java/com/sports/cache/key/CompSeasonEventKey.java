package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonEventKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;

	public CompSeasonEventKey(int competitionId, int seasonId, int compSeasonEventId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] { Integer.toString(competitionId),
				Integer.toString(seasonId),
				Integer.toString(compSeasonEventId)
		}, "|");
	}
}
