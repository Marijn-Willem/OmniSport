package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EncounterListTeamKey extends CacheDataKey {
	private final int team1Id;
	private final int team2Id;
	private final int clientId;

	public EncounterListTeamKey(int team1Id, int team2Id, int clientId) {
		this.team1Id = team1Id;
		this.team2Id = team2Id;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(team1Id), Integer.toString(team2Id),
		        Integer.toString(clientId)
		}, "|");
	}
}
