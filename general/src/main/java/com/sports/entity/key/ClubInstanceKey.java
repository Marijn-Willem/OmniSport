package com.sports.entity.key;

public class ClubInstanceKey extends EntityInstanceKey {
    public ClubInstanceKey(int entityId, int instanceId) {
        super(entityId, instanceId);
    }

    @Override
    String getEntityName() {
        return "club";
    }
}
