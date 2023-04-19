package com.sports.logic.factory;

import com.sports.entity.EntityInstance;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.entity.manager.EntityInstanceManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface EntityInstanceFactory {
    EntityInstanceManager<? extends EntityInstanceKey, ? extends EntityInstance> getManager(Statement stat);
    EntityInstanceKey getKey(int entityId, int instanceId);
    EntityInstance getInstance();
    List<CompSeasonKey> getCompSeasonsRelatedToEntity(int entityId, Statement stat) throws SQLException;
}
