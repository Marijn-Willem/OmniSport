package com.alias.servlet.util;

public class EntityMetaData {
    private final String entityName;
    private final boolean hasInstance;

    public EntityMetaData(String entityName, boolean hasInstance) {
        this.entityName = entityName;
        this.hasInstance = hasInstance;
    }

    public String getEntityName() {
        return entityName;
    }

    public boolean isHasInstance() {
        return hasInstance;
    }
}
