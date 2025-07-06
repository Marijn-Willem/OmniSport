package com.sports.cache.key;

import com.sports.logic.util.Util;

public class MatchListPhaseKey extends CacheFragmentKey {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonPhaseId;

	public MatchListPhaseKey(int competitionId, int seasonId, int compSeasonPhaseId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonPhaseId = compSeasonPhaseId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(compSeasonPhaseId)
		}, "|");
	}
}
