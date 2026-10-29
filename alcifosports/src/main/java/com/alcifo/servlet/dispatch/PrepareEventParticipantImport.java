package com.alcifo.servlet.dispatch;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.SportEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SportEventManager;
import com.sportservlet.dispatch.SuperDispatchServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class PrepareEventParticipantImport extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        CompSeasonEventKey cseKey = getCompSeasonEventKey(req);
        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);

        SportEvent sportEvent = new SportEventManager(stat).getEntityFromSuperKey(compSeasonEvent.getSportEventKey());

        if (sportEvent.isTeam())
            dispatchURL = "EventTeamImport";
        else
            dispatchURL = "EventPersonImport";
    }
}
