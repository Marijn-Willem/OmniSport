package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EncounterListDoubleKey;
import com.sports.logic.factory.CompSeasonDoubleFactory;

public class EncounterListDoubleData extends EncounterListData {
	public EncounterListDoubleData(int double1Id, int double2Id, Integer clientId) {
		super(double1Id, double2Id, clientId);
	}

	@Override
	CompSeasonDoubleFactory getCompSeasonParticipantFactory() {
		return new CompSeasonDoubleFactory();
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new EncounterListDoubleKey(participant1Id, participant2Id, clientId);
	}
}
