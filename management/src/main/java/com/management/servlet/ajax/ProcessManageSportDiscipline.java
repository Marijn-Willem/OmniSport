package com.management.servlet.ajax;

import com.sports.entity.SportDiscipline;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.SportDisciplineManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageSportDiscipline extends ProcessManageSuperKeyEntity<SportDisciplineKey, SportDiscipline> {
    protected SuperKeySuperManager<SportDisciplineKey, SportDiscipline> getSuperManager(Statement stat) {
        return new SportDisciplineManager(stat);
    }

    protected SportDisciplineKey getNewSuperKey(SuperKeySuperManager<SportDisciplineKey, SportDiscipline> superManager,
                                      HttpServletRequest req) throws SQLException {
        int spid = getIntValuedParameterValue(req, "spid");
        int did = ((SportDisciplineManager)superManager).getNewDisciplineId(spid);

        return new SportDisciplineKey(spid, did);
    }

    protected String getUpdateIdStr(SportDisciplineKey superKey) {
        return "" + superKey.getSportDisciplineId();
    }

    protected SportDisciplineKey getSuperKeyFromRequest(HttpServletRequest req) {
        int spid = getIntValuedParameterValue(req, "spid");
        int did = convertRequestParamToIdInteger(req, "did");

        return new SportDisciplineKey(spid, did);
    }

    protected SportDiscipline getNewEntity() {
        return new SportDiscipline();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        int rtid = getIntValuedParameterValue(req, "rtid");
        Integer rtpid = convertRequestParamToIdInteger(req, "rtpid");

        entity.setName(nm);
        entity.setResultTypeId(rtid);
        entity.setResultTypePrecisionId(rtpid);
    }
}
