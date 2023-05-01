package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonEventPartListKey extends CacheDataKey {
	private final int competitionId;
	private final int seasonId;
	private final int sportEventId;
	private final int clientId;

	public CompSeasonEventPartListKey(int competitionId, int seasonId, int compSeasonEventId, int clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.sportEventId = compSeasonEventId;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(sportEventId), Integer.toString(clientId)
		}, "|");
	}
}
