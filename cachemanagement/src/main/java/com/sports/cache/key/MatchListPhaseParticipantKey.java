package com.sports.cache.key;

import com.sports.logic.util.Util;

public class MatchListPhaseParticipantKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonPhaseId;
	private final int participantId;

	public MatchListPhaseParticipantKey(int competitionId, int seasonId, int compSeasonPhaseId, int participantId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonPhaseId = compSeasonPhaseId;
		this.participantId = participantId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(compSeasonPhaseId), Integer.toString(participantId)
		}, "|");
	}
}
