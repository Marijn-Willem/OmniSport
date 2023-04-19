package com.management.servlet.ajax;

import com.sports.entity.SportEventPart;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.key.SportEventPartKey;
import com.sports.entity.manager.SportEventPartManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageSportEventPart extends ProcessManageSuperKeyEntity<SportEventPartKey, SportEventPart> {
    protected SuperKeySuperManager<SportEventPartKey, SportEventPart> getSuperManager(Statement stat) {
        return new SportEventPartManager(stat);
    }

    protected SportEventPartKey getNewSuperKey(SuperKeySuperManager<SportEventPartKey, SportEventPart> superManager,
                                     HttpServletRequest req) throws SQLException {
        SportEventKey sek = getSportEventKey(req);
        int epid = ((SportEventPartManager)superManager).getNewPartId(sek);

        return new SportEventPartKey(sek, epid);
    }

    protected String getUpdateIdStr(SportEventPartKey superKey) {
        return "" + superKey.getSportEventPartId();
    }

    protected SportEventPartKey getSuperKeyFromRequest(HttpServletRequest req) {
        SportEventKey sek = getSportEventKey(req);
        int epid = convertRequestParamToIdInteger(req, "epid");

        return new SportEventPartKey(sek, epid);
    }

    protected SportEventPart getNewEntity() {
        return new SportEventPart();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        int did = getIntValuedParameterValue(req, "did");
        String nm = req.getParameter("nm");
        int o = getIntValuedParameterValue(req, "o");
        Integer w = convertRequestParamToNonIdInteger(req, "w");

        entity.setSportDisciplineId(did);
        entity.setName(nm);
        entity.setOrder(o);
        entity.setWeight(w);
    }

    private SportEventKey getSportEventKey(HttpServletRequest req) {
        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");

        return new SportEventKey(spid, eid);
    }
}
