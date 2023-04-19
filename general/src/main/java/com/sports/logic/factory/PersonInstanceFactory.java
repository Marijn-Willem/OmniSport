package com.sports.logic.factory;

import com.sports.entity.PersonInstance;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.PersonInstanceKey;
import com.sports.entity.manager.CompSeasonPersonSportManager;
import com.sports.entity.manager.EntityInstanceManager;
import com.sports.entity.manager.PersonInstanceManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonInstanceFactory implements EntityInstanceFactory {
    @Override
    public EntityInstanceManager<PersonInstanceKey, PersonInstance> getManager(Statement stat) {
        return new PersonInstanceManager(stat);
    }

    @Override
    public PersonInstanceKey getKey(int entityId, int instanceId) {
        return new PersonInstanceKey(entityId, instanceId);
    }

    @Override
    public PersonInstance getInstance() {
        return new PersonInstance();
    }

    @Override
    public List<CompSeasonKey> getCompSeasonsRelatedToEntity(int entityId, Statement stat) throws SQLException {
        return new ArrayList<>() {{
            addAll(new CompSeasonPersonSportManager(stat).getCompSeasonsForParticipants(
                    Collections.singletonList(entityId)));
        }};
    }
}
