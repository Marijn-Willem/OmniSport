package com.sportservlet.ajax;

import com.sports.entity.IntEntity;
import com.sports.entity.manager.IntSuperManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ProcessManageIntEntity<T extends IntEntity> extends ProcessManageEntity<T> {
    protected int id;
    protected T entity;

    protected abstract IntSuperManager<T> getSuperManager(Statement stat);
    protected abstract Integer getIdFromRequest(HttpServletRequest req);

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException {
        IntSuperManager<T> superManager = getSuperManager(stat);

        if ("u".equals(mode)) {
            id = getIdFromRequest(req);
            entity = superManager.getEntityFromId(id);
            processEntityFromRequest(stat, req);
            superManager.update(id, entity);
        }
        else {
            id = superManager.getNewId();
            entity = getNewEntity();
            processEntityFromRequest(stat, req);
            superManager.insert(id, entity);
        }

        updateIdStr = "" + id;
    }
}
