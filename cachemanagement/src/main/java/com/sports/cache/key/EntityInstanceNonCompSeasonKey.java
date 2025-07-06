package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EntityInstanceNonCompSeasonKey extends CacheFragmentKey {
    private final String entityName;
    private final int entityId;

    public EntityInstanceNonCompSeasonKey(String entityName, int entityId) {
        this.entityName = entityName;
        this.entityId = entityId;
    }

    @Override
    public String getSpecificKeyPart() {
        return Util.concatStringsWithDelimiter(entityName, Integer.toString(entityId), "|");
    }
}
