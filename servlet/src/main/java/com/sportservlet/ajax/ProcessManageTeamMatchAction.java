package com.sportservlet.ajax;

import com.sports.entity.TeamMatchAction;
import com.sports.entity.key.TeamMatchActionKey;
import com.sports.entity.key.TeamMatchKey;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sports.entity.manager.TeamMatchActionManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageTeamMatchAction extends ProcessManageSuperKeyEntity<TeamMatchActionKey, TeamMatchAction> {
    @Override
    protected SuperKeySuperManager<TeamMatchActionKey, TeamMatchAction> getSuperManager(Statement stat) {
        return new TeamMatchActionManager(stat);
    }

    @Override
    protected TeamMatchActionKey getNewSuperKey(SuperKeySuperManager<TeamMatchActionKey, TeamMatchAction> superManager, HttpServletRequest req) throws SQLException {
        TeamMatchKey teamMatchKey = new TeamMatchKey(compSeasonKey, getIntValuedParameterValue(req, "mid"));
        int teamMatchActionId = ((TeamMatchActionManager)superManager).getNewTeamMatchActionId(teamMatchKey);
        return new TeamMatchActionKey(teamMatchKey, teamMatchActionId);
    }

    @Override
    protected String getUpdateIdStr(TeamMatchActionKey superKey) {
        return "";
    }

    @Override
    protected TeamMatchActionKey getSuperKeyFromRequest(HttpServletRequest req) {
        return null;
    }

    @Override
    protected TeamMatchAction getNewEntity() {
        return new TeamMatchAction();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        int at = getIntValuedParameterValue(req, "at");
        int tid = getIntValuedParameterValue(req, "tid");
        int cnt = getIntValuedParameterValue(req, "cnt");

        entity.setActionTypeId(at);
        entity.setTeamId(tid);
        entity.setCount(cnt);
    }
}
