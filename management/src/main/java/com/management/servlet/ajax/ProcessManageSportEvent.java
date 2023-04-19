package com.management.servlet.ajax;

import com.sports.entity.SportEvent;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.SportEventManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageSportEvent extends ProcessManageSuperKeyEntity<SportEventKey, SportEvent> {
    protected SuperKeySuperManager<SportEventKey, SportEvent> getSuperManager(Statement stat) {
        return new SportEventManager(stat);
    }

    protected SportEventKey getNewSuperKey(SuperKeySuperManager<SportEventKey, SportEvent> superManager,
                                 HttpServletRequest req) throws SQLException {
        int spid = Integer.parseInt(req.getParameter("spid"));
        int eid = ((SportEventManager)superManager).getNewSportEventId(spid);

        return new SportEventKey(spid, eid);
    }

    protected String getUpdateIdStr(SportEventKey superKey) {
        return "" + superKey.getSportEventId();
    }

    protected SportEventKey getSuperKeyFromRequest(HttpServletRequest req) {
        int spid = Integer.parseInt(req.getParameter("spid"));
        Integer eid = convertRequestParamToIdInteger(req, "eid");

        return new SportEventKey(spid, eid);
    }

    protected SportEvent getNewEntity() {
        return new SportEvent();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        int gid = Integer.parseInt(req.getParameter("gid"));
        boolean psa = Boolean.parseBoolean(req.getParameter("psa"));
        boolean it = Boolean.parseBoolean(req.getParameter("it"));

        entity.setName(nm);
        entity.setGenderId(gid);
        entity.setPointsSortAsc(psa);
        entity.setTeam(it);
    }
}
