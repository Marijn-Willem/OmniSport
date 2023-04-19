package com.management.servlet.ajax;

import com.sports.entity.LocationRole;
import com.sports.entity.manager.LocationRoleManager;
import com.sports.entity.manager.IntSuperManager;
import com.sportservlet.ajax.ProcessManageIntEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageLocationRole extends ProcessManageIntEntity<LocationRole> {
    @Override
    protected IntSuperManager<LocationRole> getSuperManager(Statement stat) {
        return new LocationRoleManager(stat);
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "lrid");
    }

    @Override
    protected LocationRole getNewEntity() {
        return new LocationRole();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        entity.setName(req.getParameter("nm"));
    }
}
