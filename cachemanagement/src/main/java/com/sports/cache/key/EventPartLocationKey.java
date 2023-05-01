package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventPartLocationKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int sportEventId;
	private final int compSeasonEventPartId;
	private final int eventPartLocationId;

	public EventPartLocationKey(int competitionId, int seasonId, int compSeasonEventId,
								int compSeasonEventPartId, int eventPartLocationId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.sportEventId = compSeasonEventId;
		this.compSeasonEventPartId = compSeasonEventPartId;
		this.eventPartLocationId = eventPartLocationId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(sportEventId), Integer.toString(compSeasonEventPartId),
		        Integer.toString(eventPartLocationId)
		}, "|");
	}
}
