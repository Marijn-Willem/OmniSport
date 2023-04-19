package com.sports.cache.key;

import com.sports.entity.key.CompSeasonPersonSportKey;

public class PersonSportKey extends CacheKey {
    private final CompSeasonPersonSportKey compSeasonPersonSportKey;

    public PersonSportKey(CompSeasonPersonSportKey compSeasonPersonSportKey) {
        this.compSeasonPersonSportKey = compSeasonPersonSportKey;
    }

    @Override
    public String getSpecificKeyPart() {
        return compSeasonPersonSportKey.getPipeSepValues();
    }
}
