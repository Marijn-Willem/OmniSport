package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventPersonSportRankingKey extends CacheDataKey {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final int clientId;

	public EventPersonSportRankingKey(int competitionId, int seasonId, int compSeasonEventId, int clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(compSeasonEventId), Integer.toString(clientId)
		}, "|");
	}
}
