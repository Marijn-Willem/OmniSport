package com.sports.entity.manager;

import com.sports.entity.ClubInstance;
import com.sports.entity.key.ClubInstanceKey;

import java.sql.Statement;

public class ClubInstanceManager extends EntityInstanceManager<ClubInstanceKey, ClubInstance> {
    public ClubInstanceManager(Statement stat) {
        super(stat);
    }

    @Override
    ClubInstance getInstance() {
        return new ClubInstance();
    }

    @Override
    String getEntityName() {
        return "club";
    }

    @Override
    ClubInstanceKey getKey(int entityId, int instanceId) {
        return new ClubInstanceKey(entityId, instanceId);
    }
}
