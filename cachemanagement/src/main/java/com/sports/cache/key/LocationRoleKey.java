package com.sports.cache.key;

public class LocationRoleKey extends CacheKey {
	private final int locationRoleId;

	public LocationRoleKey(int locationRoleId) {
		this.locationRoleId = locationRoleId;
	}

	@Override
	String getSpecificKeyPart() {
		return Integer.toString(locationRoleId);
	}
}
