package com.sports.cache.key;

public class EventPartNameKey extends CacheKey {
	private final int eventPartNameId;

	public EventPartNameKey(int eventPartNameId) {
		this.eventPartNameId = eventPartNameId;
	}

	@Override
	String getSpecificKeyPart() {
		return String.valueOf(eventPartNameId);
	}
}
