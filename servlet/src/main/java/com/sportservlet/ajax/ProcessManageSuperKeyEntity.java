package com.sportservlet.ajax;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.SuperKeySuperManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ProcessManageSuperKeyEntity<S extends SuperKey, T extends SuperKeyEntity>
        extends ProcessManageEntity<T> {
    protected S superKey;
    protected T entity;

    protected abstract SuperKeySuperManager<S, T> getSuperManager(Statement stat);
    protected abstract S getNewSuperKey(SuperKeySuperManager<S, T> superManager, HttpServletRequest req) throws SQLException;
    protected abstract String getUpdateIdStr(S superKey);
    protected abstract S getSuperKeyFromRequest(HttpServletRequest req);

    void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException {
        SuperKeySuperManager<S, T> superManager = getSuperManager(stat);

        if ("u".equals(mode)) {
            superKey = getSuperKeyFromRequest(req);
            entity = superManager.getEntityFromSuperKey(superKey);
            processEntityFromRequest(stat, req);
            superManager.update(superKey, entity);
        }
        else {
            superKey = getNewSuperKey(superManager, req);
            entity = getNewEntity();
            processEntityFromRequest(stat, req);
            superManager.insert(superKey, entity);
        }

        updateIdStr = getUpdateIdStr(superKey);
    }
}
