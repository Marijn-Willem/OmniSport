package com.sports.entity.manager;

import com.sports.entity.GeoInstance;
import com.sports.entity.key.GeoInstanceKey;

import java.sql.Statement;

public class GeoInstanceManager extends EntityInstanceManager<GeoInstanceKey, GeoInstance> {
    public GeoInstanceManager(Statement stat) {
        super(stat);
    }

    @Override
    GeoInstance getInstance() {
        return new GeoInstance();
    }

    @Override
    String getEntityName() {
        return "geo";
    }

    @Override
    GeoInstanceKey getKey(int entityId, int instanceId) {
        return new GeoInstanceKey(entityId, instanceId);
    }
}
