package com.sports.entity.manager;

import com.sports.entity.PersonInstance;
import com.sports.entity.key.PersonInstanceKey;

import java.sql.ResultSet;
import java.sql.Statement;

public class PersonInstanceManager extends EntityInstanceManager<PersonInstanceKey, PersonInstance> {
    public PersonInstanceManager(Statement stat) {
        super(stat);
    }

    @Override
    PersonInstance getInstance() {
        return new PersonInstance();
    }

    @Override
    String getEntityName() {
        return "person";
    }

    @Override
    PersonInstanceKey getKey(int entityId, int instanceId) {
        return new PersonInstanceKey(entityId, instanceId);
    }

    @Override
    String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    void fillSpecificPropertiesFromResultSet(ResultSet rs, PersonInstance entityInstance) { }
}
