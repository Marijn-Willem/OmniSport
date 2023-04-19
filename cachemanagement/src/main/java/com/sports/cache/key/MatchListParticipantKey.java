package com.sports.cache.key;

import com.sports.logic.util.Util;

public class MatchListParticipantKey extends CacheDataKey {
	private final int competitionId;
	private final int seasonId;
	private final int participantId;
	private final int clientId;

	public MatchListParticipantKey(int competitionId, int seasonId, int participantId, int clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.participantId = participantId;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
		        Integer.toString(participantId), Integer.toString(clientId)
		}, "|");
	}
}
