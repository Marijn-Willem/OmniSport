package com.sports.entity.key;

public abstract class EntityInstanceKey extends SuperKey {
    private final int entityId;
    private final int instanceId;

    abstract String getEntityName();

    public EntityInstanceKey(int entityId, int instanceId) {
        this.entityId = entityId;
        this.instanceId = instanceId;
    }

    @Override
    public int hashCode() {
        return 100 * entityId + instanceId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(getClass()) &&
                ((EntityInstanceKey)obj).entityId == entityId &&
                ((EntityInstanceKey)obj).instanceId == instanceId;
    }

    @Override
    public String getWhereClause() {
        return getEntityName() + "id = " + entityId + " AND " + getEntityName() + "instanceid = " + instanceId;
    }

    @Override
    public String getSepValues(String delim) {
        return entityId + delim + instanceId;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getInstanceId() {
        return instanceId;
    }
}
