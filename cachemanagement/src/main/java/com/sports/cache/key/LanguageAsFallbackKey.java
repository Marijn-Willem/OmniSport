package com.sports.cache.key;

public class LanguageAsFallbackKey extends CacheFragmentKey {
	private final int languageId;

	public LanguageAsFallbackKey(int languageId) {
		this.languageId = languageId;
	}

	@Override
	String getSpecificKeyPart() {
		return Integer.toString(languageId);
	}
}
