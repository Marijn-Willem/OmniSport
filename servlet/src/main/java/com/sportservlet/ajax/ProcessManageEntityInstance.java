package com.sportservlet.ajax;

import com.sports.entity.EntityInstance;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.EntityInstanceFlusher;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

abstract class ProcessManageEntityInstance<EIK extends EntityInstanceKey, EI extends EntityInstance> extends ProcessManageSuperKeyEntity<EIK, EI> {
    private String entityName;
    private EntityInstanceFactory<EIK, EI> entityInstanceFactory;

    abstract EntityInstanceFactory<EIK, EI> getEntityInstanceFactory();
    abstract void processSpecificFields(Statement stat, EI entityInstance, HttpServletRequest req) throws SQLException;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {
        entityName = req.getParameter("enm");
        entityInstanceFactory = getEntityInstanceFactory();
    }

    @Override
    protected SuperKeySuperManager<EIK, EI> getSuperManager(Statement stat) {
        return entityInstanceFactory.getManager(stat);
    }

    @Override
    protected EIK getNewSuperKey(SuperKeySuperManager<EIK, EI> superManager, HttpServletRequest req) {
        return null;
    }

    @Override
    protected String getUpdateIdStr(EIK superKey) {
        return Integer.toString(superKey.getInstanceId());
    }

    @Override
    protected EIK getSuperKeyFromRequest(HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");
        int eiid = getIntValuedParameterValue(req, "eiid");

        return entityInstanceFactory.getKey(eid, eiid);
    }

    @Override
    protected EI getNewEntity() {
        return entityInstanceFactory.getInstance();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException{
        String nm = req.getParameter("nm");

        entity.setName(nm);
        processSpecificFields(stat, entity, req);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new EntityInstanceFlusher(entityName, superKey.getEntityId());
    }
}
