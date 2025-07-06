package com.sports.cache.key;

public class ResultTypeKey extends CacheFragmentKey {
	private final int resultTypeId;

	public ResultTypeKey(int resultTypeId) {
		this.resultTypeId = resultTypeId;
	}

	@Override
	String getSpecificKeyPart() {
		return Integer.toString(resultTypeId);
	}
}
