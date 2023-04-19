package com.management.servlet.ajax;

import com.sports.entity.NoCountResult;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.NoCountResultManager;
import com.sportservlet.ajax.ProcessManageIntEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageNoCountResult extends ProcessManageIntEntity<NoCountResult> {
    @Override
    protected NoCountResult getNewEntity() {
        return new NoCountResult();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        String nm = req.getParameter("nm");
        entity.setName(nm);
    }

    @Override
    protected IntSuperManager<NoCountResult> getSuperManager(Statement stat) {
        return new NoCountResultManager(stat);
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "ncrid");
    }
}
