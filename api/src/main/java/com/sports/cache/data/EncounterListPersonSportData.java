package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EncounterListPersonSportKey;
import com.sports.logic.factory.CompSeasonPersonSportFactory;

public class EncounterListPersonSportData extends EncounterListData {
	public EncounterListPersonSportData(int personSport1Id, int personSport2Id, Integer clientId) {
		super(personSport1Id, personSport2Id, clientId);
	}

	@Override
	CompSeasonPersonSportFactory getCompSeasonParticipantFactory() {
		return new CompSeasonPersonSportFactory();
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new EncounterListPersonSportKey(participant1Id, participant2Id, clientId);
	}
}
