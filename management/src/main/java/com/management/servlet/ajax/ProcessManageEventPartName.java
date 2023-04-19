package com.management.servlet.ajax;

import com.sports.entity.EventPartName;
import com.sports.entity.key.EventPartNameKey;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.EventPartNameManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageEventPartName extends ProcessManageSuperKeyEntity<EventPartNameKey, EventPartName> {
    @Override
    protected EventPartName getNewEntity() {
        return new EventPartName();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        String nm = req.getParameter("nm");

        entity.setName(nm);
    }

    @Override
    protected SuperKeySuperManager<EventPartNameKey, EventPartName> getSuperManager(Statement stat) {
        return new EventPartNameManager(stat);
    }

    @Override
    protected EventPartNameKey getNewSuperKey(SuperKeySuperManager<EventPartNameKey, EventPartName> superManager, HttpServletRequest req) throws SQLException {
        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");
        SportEventKey sportEventKey = new SportEventKey(spid, eid);

        int epnid = ((EventPartNameManager)superManager).getNewEventPartNameId(sportEventKey);

        return new EventPartNameKey(sportEventKey, epnid);
    }

    @Override
    protected String getUpdateIdStr(EventPartNameKey superKey) {
        return Integer.toString(superKey.getEventPartNameId());
    }

    @Override
    protected EventPartNameKey getSuperKeyFromRequest(HttpServletRequest req) {
        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");
        int epnid = getIntValuedParameterValue(req, "epnid");

        return new EventPartNameKey(new SportEventKey(spid, eid), epnid);
    }
}
