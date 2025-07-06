package com.sports.cache.key;

public class LanguageKey extends CacheFragmentKey {
	private final int languageId;

	public LanguageKey(int languageId) {
		this.languageId = languageId;
	}

	@Override
	String getSpecificKeyPart() {
		return Integer.toString(languageId);
	}
}
