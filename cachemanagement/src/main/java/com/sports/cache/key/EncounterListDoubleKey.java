package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EncounterListDoubleKey extends CacheDataKey {
	private final int double1Id;
	private final int double2Id;
	private final int clientId;

	public EncounterListDoubleKey(int double1Id, int double2Id, int clientId) {
		this.double1Id = double1Id;
		this.double2Id = double2Id;
		this.clientId = clientId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(double1Id), Integer.toString(double2Id),
		        Integer.toString(clientId)
		}, "|");
	}
}
