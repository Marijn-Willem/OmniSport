package com.sports.entity.key;

public class EquipeInstanceKey extends EntityInstanceKey {
    public EquipeInstanceKey(int entityId, int instanceId) {
        super(entityId, instanceId);
    }

    @Override
    String getEntityName() {
        return "equipe";
    }
}
