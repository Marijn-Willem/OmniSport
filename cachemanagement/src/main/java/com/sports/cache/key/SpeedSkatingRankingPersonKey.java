package com.sports.cache.key;

import com.sports.logic.util.Util;

public class SpeedSkatingRankingPersonKey extends CacheFragmentKey {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final int compSeasonEventPartId;
	private final int personSportId;

	public SpeedSkatingRankingPersonKey(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId, int personSportId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.compSeasonEventPartId = compSeasonEventPartId;
		this.personSportId = personSportId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(compSeasonEventId), Integer.toString(compSeasonEventPartId),
		        Integer.toString(personSportId)
		}, "|");
	}
}
