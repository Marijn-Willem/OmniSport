package com.management.servlet.ajax;

import com.sports.entity.EventPartName;
import com.sports.entity.manager.EventPartNameManager;
import com.sports.entity.manager.IntSuperManager;
import com.sportservlet.ajax.ProcessManageIntEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageEventPartName extends ProcessManageIntEntity<EventPartName> {
    @Override
    protected EventPartName getNewEntity() {
        return new EventPartName();
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return getIntValuedParameterValue(req, "epnid");
    }

    @Override
    protected IntSuperManager<EventPartName> getSuperManager(Statement stat) {
        return new EventPartNameManager(stat);
    }
    
    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        String nm = req.getParameter("nm");

        entity.setName(nm);
    }
}
