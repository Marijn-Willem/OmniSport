package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CyclingRoadPersonResultsKey extends CacheDataKey {
	private final int personSportId;
	private final int seasonId;
	private final int clientId;

	public CyclingRoadPersonResultsKey(int personSportId, int seasonId, int clientId) {
		this.personSportId = personSportId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(personSportId), Integer.toString(seasonId),
		        Integer.toString(clientId)
		}, "|");
	}
}
