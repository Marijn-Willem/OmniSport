package com.sportservlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SuperKeySuperManager;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageCompSeasonEvent extends ProcessManageSuperKeyEntity<CompSeasonEventKey, CompSeasonEvent> {
    @Override
    protected CompSeasonEvent getNewEntity() {
        return new CompSeasonEvent();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        String es = req.getParameter("es");

        entity.setExternalSource(es);
    }

    @Override
    protected SuperKeySuperManager<CompSeasonEventKey, CompSeasonEvent> getSuperManager(Statement stat) {
        return new CompSeasonEventManager(stat);
    }

    @Override
    protected CompSeasonEventKey getNewSuperKey(SuperKeySuperManager<CompSeasonEventKey, CompSeasonEvent> superManager,
                                                HttpServletRequest req) {
        return null;
    }

    @Override
    protected String getUpdateIdStr(CompSeasonEventKey superKey) {
        return Integer.toString(superKey.getCompSeasonEventId());
    }

    @Override
    protected CompSeasonEventKey getSuperKeyFromRequest(HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");

        return new CompSeasonEventKey(compSeasonKey, eid);
    }
}
