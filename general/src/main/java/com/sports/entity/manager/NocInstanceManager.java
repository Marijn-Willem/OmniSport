package com.sports.entity.manager;

import com.sports.entity.NocInstance;
import com.sports.entity.key.NocInstanceKey;

import java.sql.ResultSet;
import java.sql.Statement;

public class NocInstanceManager extends EntityInstanceManager<NocInstanceKey, NocInstance> {
    public NocInstanceManager(Statement stat) {
        super(stat);
    }

    @Override
    NocInstance getInstance() {
        return new NocInstance();
    }

    @Override
    String getEntityName() {
        return "noc";
    }

    @Override
    NocInstanceKey getKey(int entityId, int instanceId) {
        return new NocInstanceKey(entityId, instanceId);
    }

    @Override
    String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    void fillSpecificPropertiesFromResultSet(ResultSet rs, NocInstance entityInstance) { }
}
