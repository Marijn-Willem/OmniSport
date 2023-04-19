package com.sports.entity.key;

public class AliasEntityIdKey extends SuperKey {
    private int aliasEntityId;
    private String entityId;

    public AliasEntityIdKey(int aliasEntityId, String entityId) {
        this.aliasEntityId = aliasEntityId;
        this.entityId = entityId;
    }

    @Override
    public int hashCode() {
        return 100 * entityId.hashCode() + aliasEntityId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AliasEntityIdKey &&
                ((AliasEntityIdKey)obj).aliasEntityId == aliasEntityId &&
                ((AliasEntityIdKey)obj).entityId.equals(entityId);
    }

    public String getWhereClause() {
        return "aliasentityid = " + aliasEntityId + " AND entityid = '" + entityId + "'";
    }

    public String getSepValues(String delim) {
        return aliasEntityId + delim + entityId;
    }

    public int getAliasEntityId() {
        return aliasEntityId;
    }

    public String getEntityId() {
        return entityId;
    }
}
