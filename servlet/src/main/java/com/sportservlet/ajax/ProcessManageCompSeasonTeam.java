package com.sportservlet.ajax;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.SuperKeySuperManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageCompSeasonTeam extends ProcessManageSuperKeyEntity<CompSeasonTeamKey, CompSeasonTeam> {
    @Override
    protected SuperKeySuperManager<CompSeasonTeamKey, CompSeasonTeam> getSuperManager(Statement stat) {
        return new CompSeasonTeamManager(stat);
    }

    @Override
    protected CompSeasonTeamKey getNewSuperKey(SuperKeySuperManager<CompSeasonTeamKey, CompSeasonTeam> superManager, HttpServletRequest req) {
        return null;
    }

    @Override
    protected String getUpdateIdStr(CompSeasonTeamKey superKey) {
        return "" + superKey.getSpecificId();
    }

    @Override
    protected CompSeasonTeamKey getSuperKeyFromRequest(HttpServletRequest req) {
        return new CompSeasonTeamKey(compSeasonKey, getIntValuedParameterValue(req, "tid"));
    }

    @Override
    protected CompSeasonTeam getNewEntity() {
        return new CompSeasonTeam();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        Integer did = convertRequestParamToIdInteger(req, "did");
        entity.setCompDivisionId(did);
    }
}
