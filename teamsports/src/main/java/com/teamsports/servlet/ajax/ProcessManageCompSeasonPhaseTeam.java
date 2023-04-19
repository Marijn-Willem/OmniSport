package com.teamsports.servlet.ajax;

import com.sports.entity.CompSeasonPhaseTeam;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.CompSeasonPhaseTeamManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageCompSeasonPhaseTeam extends ProcessManageSuperKeyEntity<CompSeasonPhaseTeamKey, CompSeasonPhaseTeam> {
    @Override
    protected SuperKeySuperManager<CompSeasonPhaseTeamKey, CompSeasonPhaseTeam> getSuperManager(Statement stat) {
        return new CompSeasonPhaseTeamManager(stat);
    }

    @Override
    protected CompSeasonPhaseTeamKey getNewSuperKey(SuperKeySuperManager<CompSeasonPhaseTeamKey, CompSeasonPhaseTeam> superManager, HttpServletRequest req) {
        return null;
    }

    @Override
    protected String getUpdateIdStr(CompSeasonPhaseTeamKey superKey) {
        return "";
    }

    @Override
    protected CompSeasonPhaseTeamKey getSuperKeyFromRequest(HttpServletRequest req) {
        int pid = getIntValuedParameterValue(req, "pid");
        int tid = getIntValuedParameterValue(req, "tid");

        return new CompSeasonPhaseTeamKey(new CompSeasonPhaseKey(compSeasonKey, pid), tid);
    }

    @Override
    protected CompSeasonPhaseTeam getNewEntity() {
        return new CompSeasonPhaseTeam();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        int pc = getIntValuedParameterValue(req, "pc");

        entity.setPointsCorrection(pc);
    }
}
