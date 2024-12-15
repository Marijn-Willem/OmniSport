package com.teamsports.servlet.ajax;

import com.sports.entity.CompSeasonPhaseTeamCorrection;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamCorrectionKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.CompSeasonPhaseTeamCorrectionManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sports.logic.util.Util;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageCompSeasonPhaseTeamCorrection extends ProcessManageSuperKeyEntity<CompSeasonPhaseTeamCorrectionKey, CompSeasonPhaseTeamCorrection> {
    @Override
    protected CompSeasonPhaseTeamCorrectionManager getSuperManager(Statement stat) {
        return new CompSeasonPhaseTeamCorrectionManager(stat);
    }

    @Override
    protected CompSeasonPhaseTeamCorrectionKey getNewSuperKey(SuperKeySuperManager<CompSeasonPhaseTeamCorrectionKey, CompSeasonPhaseTeamCorrection> superManager, HttpServletRequest req) throws SQLException {
        CompSeasonPhaseTeamKey compSeasonPhaseTeamKey = getCompSeasonPhaseTeamKeyFromRequest(req);

        int csptcid = ((CompSeasonPhaseTeamCorrectionManager)superManager)
                .getNewCompSeasonPhaseTeamCorrectionId(compSeasonPhaseTeamKey);

        return new CompSeasonPhaseTeamCorrectionKey(compSeasonPhaseTeamKey, csptcid);
    }

    @Override
    protected String getUpdateIdStr(CompSeasonPhaseTeamCorrectionKey superKey) {
        return Integer.toString(superKey.getCompSeasonPhaseTeamCorrectionId());
    }

    @Override
    protected CompSeasonPhaseTeamCorrectionKey getSuperKeyFromRequest(HttpServletRequest req) {
        return new CompSeasonPhaseTeamCorrectionKey(getCompSeasonPhaseTeamKeyFromRequest(req),
                getIntValuedParameterValue(req, "csptcid"));
    }

    @Override
    protected CompSeasonPhaseTeamCorrection getNewEntity() {
        return new CompSeasonPhaseTeamCorrection();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        entity.setDate(Util.convertStringToDateTime(req.getParameter("dt")));
        entity.setPointsCorrection(Integer.parseInt(req.getParameter("pc")));
    }

    private CompSeasonPhaseTeamKey getCompSeasonPhaseTeamKeyFromRequest(HttpServletRequest req) {
        int pid = getIntValuedParameterValue(req, "pid");
        int tid = getIntValuedParameterValue(req, "tid");

        return new CompSeasonPhaseTeamKey(new CompSeasonPhaseKey(compSeasonKey, pid), tid);
    }
}
