package com.management.servlet.ajax;

import com.sports.entity.DisciplinePart;
import com.sports.entity.key.DisciplinePartKey;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.DisciplinePartManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageDisciplinePart extends ProcessManageSuperKeyEntity<DisciplinePartKey, DisciplinePart> {
    protected DisciplinePart getNewEntity() {
        return new DisciplinePart();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        int o = getIntValuedParameterValue(req, "o");

        entity.setName(nm);
        entity.setOrder(o);
    }

    protected SuperKeySuperManager<DisciplinePartKey, DisciplinePart> getSuperManager(Statement stat) {
        return new DisciplinePartManager(stat);
    }

    protected DisciplinePartKey getNewSuperKey(SuperKeySuperManager<DisciplinePartKey, DisciplinePart> superManager,
                                     HttpServletRequest req) throws SQLException {
        SportDisciplineKey sdk = getSportDisciplineKey(req);

        int dpid = ((DisciplinePartManager)superManager).getNewDisciplinePartId(sdk);
        return new DisciplinePartKey(sdk, dpid);
    }

    protected String getUpdateIdStr(DisciplinePartKey superKey) {
        return "" + superKey.getDisciplinePartId();
    }

    protected DisciplinePartKey getSuperKeyFromRequest(HttpServletRequest req) {
        SportDisciplineKey sdk = getSportDisciplineKey(req);

        return new DisciplinePartKey(sdk, convertRequestParamToIdInteger(req, "dpid"));
    }

    private SportDisciplineKey getSportDisciplineKey(HttpServletRequest req) {
        int spid = getIntValuedParameterValue(req, "spid");
        int did = getIntValuedParameterValue(req, "did");

        return new SportDisciplineKey(spid, did);
    }
}
