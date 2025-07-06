package com.sports.cache.key;

public class NoCountResultKey extends CacheFragmentKey {
	private final int noCountResultId;

	public NoCountResultKey(int noCountResultId) {
		this.noCountResultId = noCountResultId;
	}

	@Override
	String getSpecificKeyPart() {
		return Integer.toString(noCountResultId);
	}
}
