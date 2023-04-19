package com.sports.cache.key;

import com.sports.logic.util.Util;

public class MatchListKey extends CacheDataKey {
	private final int competitionId;
	private final int seasonId;
	private final int clientId;

	public MatchListKey(int competitionId, int seasonId, int clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(clientId)
		}, "|");
	}
}
