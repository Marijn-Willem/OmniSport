package com.sports.cache.key;

public class CompDivisionKey extends CacheFragmentKey {
    private final com.sports.entity.key.CompDivisionKey compDivisionKey;

    public CompDivisionKey(com.sports.entity.key.CompDivisionKey compDivisionKey) {
        this.compDivisionKey = compDivisionKey;
    }

    @Override
    public String getSpecificKeyPart() {
        return compDivisionKey.getPipeSepValues();
    }
}
