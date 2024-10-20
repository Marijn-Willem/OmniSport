package com.sports.entity.manager;

import com.sports.entity.EquipeInstance;
import com.sports.entity.key.EquipeInstanceKey;

import java.sql.ResultSet;
import java.sql.Statement;

public class EquipeInstanceManager extends EntityInstanceManager<EquipeInstanceKey, EquipeInstance> {
    public EquipeInstanceManager(Statement stat) {
        super(stat);
    }

    @Override
    String getEntityName() {
        return "equipe";
    }

    @Override
    EquipeInstanceKey getKey(int entityId, int instanceId) {
        return new EquipeInstanceKey(entityId, instanceId);
    }

    @Override
    EquipeInstance getInstance() {
        return new EquipeInstance();
    }

    @Override
    String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    void fillSpecificPropertiesFromResultSet(ResultSet rs, EquipeInstance entityInstance) { }
}
