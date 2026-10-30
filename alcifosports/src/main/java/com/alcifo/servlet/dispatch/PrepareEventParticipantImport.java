package com.alcifo.servlet.dispatch;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.key.CompSeasonEventKey;
import com.sportservlet.dispatch.SuperDispatchServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class PrepareEventParticipantImport extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        CompSeasonEventKey cseKey = getCompSeasonEventKey(req);

        if (new DbCalculation(stat).getSportEvent(cseKey).isTeam())
            dispatchURL = "EventTeamImport";
        else
            dispatchURL = "EventPersonImport";
    }
}
