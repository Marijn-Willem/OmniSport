package com.sports.entity.key;

public class AliasKey extends SuperKey {
    private int aliasEntityId;
    private int aliasId;

    public AliasKey(int aliasEntityId, int aliasId) {
        this.aliasEntityId = aliasEntityId;
        this.aliasId = aliasId;
    }

    @Override
    public int hashCode() {
        return 1000 * aliasEntityId + aliasId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AliasKey &&
                ((AliasKey)obj).aliasEntityId == aliasEntityId &&
                ((AliasKey)obj).aliasId == aliasId;
    }

    @Override
    public String getWhereClause() {
        return "aliasentityid = " + aliasEntityId + " AND aliasid = " + aliasId;
    }

    @Override
    public String getSepValues(String delim) {
        return aliasEntityId + delim + aliasId;
    }

    public int getAliasEntityId() {
        return aliasEntityId;
    }

    public int getAliasId() {
        return aliasId;
    }
}
