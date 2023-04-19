package com.teamsports.servlet.ajax;

import com.sports.entity.TeamMatch;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageH2HMatch extends com.sportservlet.ajax.ProcessManageH2HMatch {
    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        super.processEntityFromRequest(stat, req);

        Integer p1ss = convertRequestParamToNonIdInteger(req, "p1ss");
        Integer p2ss = convertRequestParamToNonIdInteger(req, "p2ss");

        ((TeamMatch)entity).setScoreShootoutHome(p1ss);
        ((TeamMatch)entity).setScoreShootoutAway(p2ss);
    }
}
