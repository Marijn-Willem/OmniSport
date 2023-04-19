package com.management.servlet.ajax;

import com.sports.entity.CompDivision;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.manager.CompDivisionManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageCompDivision extends ProcessManageSuperKeyEntity<CompDivisionKey, CompDivision> {
    @Override
    protected SuperKeySuperManager<CompDivisionKey, CompDivision> getSuperManager(Statement stat) {
        return new CompDivisionManager(stat);
    }

    @Override
    protected CompDivisionKey getNewSuperKey(SuperKeySuperManager<CompDivisionKey, CompDivision> superManager, HttpServletRequest req) throws SQLException {
        int newDid = ((CompDivisionManager)superManager).getNewDivisionId(competitionId);
        return new CompDivisionKey(competitionId, newDid);
    }

    @Override
    protected String getUpdateIdStr(CompDivisionKey superKey) {
        return "" + superKey.getCompDivisionId();
    }

    @Override
    protected CompDivisionKey getSuperKeyFromRequest(HttpServletRequest req) {
        int did = getIntValuedParameterValue(req, "did");
        return new CompDivisionKey(competitionId, did);
    }

    @Override
    protected CompDivision getNewEntity() {
        return new CompDivision();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        Integer pdid = convertRequestParamToIdInteger(req, "pdid");

        entity.setName(nm);
        entity.setParentDivisionId(pdid);
    }
}
