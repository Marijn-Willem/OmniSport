package com.sports.cache.key;

import com.sports.logic.util.Util;

public class DartsPremierLeagueStandingEvolutionKey extends CacheDataKey {
	private final int seasonId;
	private final int clientId;

	public DartsPremierLeagueStandingEvolutionKey(int seasonId, int clientId) {
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStringsWithDelimiter(Integer.toString(seasonId), Integer.toString(clientId), "|");
	}
}
