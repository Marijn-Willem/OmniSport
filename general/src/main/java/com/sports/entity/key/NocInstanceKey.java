package com.sports.entity.key;

public class NocInstanceKey extends EntityInstanceKey {
    public NocInstanceKey(int entityId, int instanceId) {
        super(entityId, instanceId);
    }

    @Override
    String getEntityName() {
        return "noc";
    }
}
