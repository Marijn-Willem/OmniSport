package com.sports.cache.key;

public class SportKey extends CacheFragmentKey {
    private final int sportId;

    public SportKey(int sportId) {
        this.sportId = sportId;
    }

    @Override
    public String getSpecificKeyPart() {
        return Integer.toString(sportId);
    }
}
