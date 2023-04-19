package com.sports.entity.key;

public class PersonInstanceKey extends EntityInstanceKey {
    public PersonInstanceKey(int entityId, int instanceId) {
        super(entityId, instanceId);
    }

    @Override
    String getEntityName() {
        return "person";
    }
}
