package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EncounterListTeamKey;
import com.sports.logic.factory.CompSeasonTeamFactory;

public class EncounterListTeamData extends EncounterListData {
	public EncounterListTeamData(int team1Id, int team2Id, Integer clientId) {
		super(team1Id, team2Id, clientId);
	}

	@Override
	CompSeasonTeamFactory getCompSeasonParticipantFactory() {
		return new CompSeasonTeamFactory();
	}

	@Override
	public CacheDataKey getCacheDataKey() {
		return new EncounterListTeamKey(participant1Id, participant2Id, clientId);
	}
}
