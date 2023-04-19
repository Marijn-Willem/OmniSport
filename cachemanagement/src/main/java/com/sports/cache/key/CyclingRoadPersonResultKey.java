package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CyclingRoadPersonResultKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int sportEventId;
	private final int compSeasonEventPartId;
	private final int personSportId;

	public CyclingRoadPersonResultKey(int competitionId, int seasonId, int sportEventId, int compSeasonEventPartId, int personSportId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.sportEventId = sportEventId;
		this.compSeasonEventPartId = compSeasonEventPartId;
		this.personSportId = personSportId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(sportEventId), Integer.toString(compSeasonEventPartId),
		        Integer.toString(personSportId)
		}, "|");
	}
}
