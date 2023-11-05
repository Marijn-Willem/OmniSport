package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EncounterListPersonSportKey extends CacheDataKey {
	private final int personSport1Id;
	private final int personSport2Id;
	private final int clientId;

	public EncounterListPersonSportKey(int personSport1Id, int personSport2Id, int clientId) {
		this.personSport1Id = personSport1Id;
		this.personSport2Id = personSport2Id;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(personSport1Id), Integer.toString(personSport2Id),
		        Integer.toString(clientId)
		}, "|");
	}
}
