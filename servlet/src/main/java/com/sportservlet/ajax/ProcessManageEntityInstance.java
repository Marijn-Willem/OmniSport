package com.sportservlet.ajax;

import com.sports.entity.EntityInstance;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.EntityInstanceFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageEntityInstance extends ProcessManageSuperKeyEntity<EntityInstanceKey, EntityInstance> {
    private String entityName;
    private EntityInstanceFactory entityInstanceFactory;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {
        entityName = req.getParameter("enm");
        entityInstanceFactory = Calculation.getEntityInstanceFactory(entityName);
    }

    @Override
    protected SuperKeySuperManager<EntityInstanceKey, EntityInstance> getSuperManager(Statement stat) {
        return (SuperKeySuperManager<EntityInstanceKey, EntityInstance>) entityInstanceFactory.getManager(stat);
    }

    @Override
    protected EntityInstanceKey getNewSuperKey(SuperKeySuperManager<EntityInstanceKey, EntityInstance> superManager, HttpServletRequest req) {
        return null;
    }

    @Override
    protected String getUpdateIdStr(EntityInstanceKey superKey) {
        return Integer.toString(superKey.getInstanceId());
    }

    @Override
    protected EntityInstanceKey getSuperKeyFromRequest(HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");
        int eiid = getIntValuedParameterValue(req, "eiid");

        return entityInstanceFactory.getKey(eid, eiid);
    }

    @Override
    protected EntityInstance getNewEntity() {
        return entityInstanceFactory.getInstance();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");

        entity.setName(nm);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new EntityInstanceFlusher(entityName, superKey.getEntityId());
    }
}
