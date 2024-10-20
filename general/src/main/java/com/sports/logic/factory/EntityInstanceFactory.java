package com.sports.logic.factory;

import com.sports.entity.EntityInstance;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.entity.manager.EntityInstanceManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public abstract class EntityInstanceFactory<EIK extends EntityInstanceKey, EI extends EntityInstance> {
    private EntityInstanceManager<EIK, EI> manager;

    private EntityInstanceManager<EIK, EI> getCachedManager(Statement stat) {
        if (manager == null)
            manager = getManager(stat);

        return manager;
    }

    public abstract EntityInstanceManager<EIK, EI> getManager(Statement stat);
    public abstract EI getInstance();
    public abstract EIK getKey(int entityId, int instanceId);
    public abstract String getManagePath();
    public abstract String getProcessManagePath();
    public abstract List<CompSeasonKey> getCompSeasonsRelatedToEntity(int entityId, Statement stat) throws SQLException;

    public void processInsert(Statement stat, int eid, LocalDateTime dt) throws SQLException {
        EntityInstanceManager<EIK, EI> manager = getCachedManager(stat);

        EI currentInstance = manager.getCurrentInstance(eid);
        currentInstance.setEndDate(dt.minusSeconds(1L));

        manager.update(getKey(eid, currentInstance.getEntityInstanceId()), currentInstance);

        EI newInstance = getInstance();
        newInstance.setStartDate(dt);
        newInstance.setName(currentInstance.getName());

        manager.insert(getKey(eid, currentInstance.getEntityInstanceId() + 1), newInstance);
    }

    public EI getEntity(Statement stat, int entityId, int instanceId) throws SQLException {
        return getCachedManager(stat).getEntityFromSuperKey(getKey(entityId, instanceId));
    }
}
