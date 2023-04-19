package com.sports.cache.key;

import com.sports.entity.key.CompSeasonDoubleKey;

public class DoubleKey extends CacheKey {
    private final CompSeasonDoubleKey compSeasonDoubleKey;

    public DoubleKey(CompSeasonDoubleKey compSeasonDoubleKey) {
        this.compSeasonDoubleKey = compSeasonDoubleKey;
    }

    @Override
    public String getSpecificKeyPart() {
        return compSeasonDoubleKey.getPipeSepValues();
    }
}
