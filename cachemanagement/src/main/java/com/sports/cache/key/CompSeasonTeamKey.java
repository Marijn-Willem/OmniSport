package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonTeamKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int teamId;

	public CompSeasonTeamKey(int competitionId, int seasonId, int teamId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.teamId = teamId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(teamId)
		}, "|");
	}
}
