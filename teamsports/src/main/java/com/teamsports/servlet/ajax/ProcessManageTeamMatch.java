package com.teamsports.servlet.ajax;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ProcessManageTeamMatch extends com.sportservlet.ajax.ProcessManageTeamMatch {
    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        super.processEntityFromRequest(stat, req);

        Integer p1ss = convertRequestParamToNonIdInteger(req, "p1ss");
        Integer p2ss = convertRequestParamToNonIdInteger(req, "p2ss");

        entity.setScoreShootoutHome(p1ss);
        entity.setScoreShootoutAway(p2ss);
    }
}
