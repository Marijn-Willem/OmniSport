package com.sports.entity.key;

public class GeoInstanceKey extends EntityInstanceKey {
    public GeoInstanceKey(int entityId, int instanceId) {
        super(entityId, instanceId);
    }

    @Override
    String getEntityName() {
        return "geo";
    }
}
