package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventPersonSportForRankingKey extends CacheFragmentKey {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final int personSportId;

	public EventPersonSportForRankingKey(int competitionId, int seasonId, int compSeasonEventId, int personSportId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.personSportId = personSportId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(compSeasonEventId), Integer.toString(personSportId)
		}, "|");
	}
}
