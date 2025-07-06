package com.sports.cache.key;

import com.sports.logic.util.Util;

public class MatchListCompSeasonKey extends CacheFragmentKey {
	private final int competitionId;
	private final int seasonId;

	public MatchListCompSeasonKey(int competitionId, int seasonId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStringsWithDelimiter(Integer.toString(competitionId), Integer.toString(seasonId), "|");
	}
}
