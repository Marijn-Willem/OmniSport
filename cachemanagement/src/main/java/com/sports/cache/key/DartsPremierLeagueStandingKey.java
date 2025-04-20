package com.sports.cache.key;

import com.sports.logic.util.Util;

public class DartsPremierLeagueStandingKey extends CacheDataKey {
	private final int seasonId;
	private final int clientId;

	public DartsPremierLeagueStandingKey(int seasonId, int clientId) {
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStringsWithDelimiter(Integer.toString(seasonId), Integer.toString(clientId), "|");
	}
}
