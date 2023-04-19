package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventPartNameKey extends CacheKey {
	private final int sportId;
	private final int sportEventId;
	private final int eventPartNameId;

	public EventPartNameKey(int sportId, int sportEventId, int eventPartNameId) {
		this.sportId = sportId;
		this.sportEventId = sportEventId;
		this.eventPartNameId = eventPartNameId;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(sportId), Integer.toString(sportEventId),
		        Integer.toString(eventPartNameId)
		}, "|");
	}
}
