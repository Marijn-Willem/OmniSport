package com.sports.cache.key;

import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.util.Util;

public class EntityInstanceCompSeasonKey extends CacheFragmentKey {
    private final String entityName;
    private final int entityId;
    private final CompSeasonKey compSeasonKey;

    public EntityInstanceCompSeasonKey(String entityName, int entityId, CompSeasonKey compSeasonKey) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.compSeasonKey = compSeasonKey;
    }

    @Override
    public String getSpecificKeyPart() {
        return Util.concatStrings(new String[] { entityName, Integer.toString(entityId),
                compSeasonKey.getPipeSepValues()}, "|");
    }
}
