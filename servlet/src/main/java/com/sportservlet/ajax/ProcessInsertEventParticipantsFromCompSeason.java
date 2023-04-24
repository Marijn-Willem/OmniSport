package com.sportservlet.ajax;

import com.sports.calc.alcifo.Calculation;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.SportEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SportEventManager;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProcessInsertEventParticipantsFromCompSeason extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEventKey compSeasonEventKey = getCompSeasonEventKey(req);
        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(compSeasonEventKey);
        SportEvent sportEvent = new SportEventManager(stat).getEntityFromSuperKey(compSeasonEvent.getSportEventKey());

        List<Integer> participantIds = Calculation.getAlcifoParticipantFactory(sportEvent).getManager(stat)
                .getParticipantIdsInEvent(compSeasonEventKey);

        String output;

        if (participantIds.size() == 0) {
            new DbCalculation(stat).insertParticipantsFromCompSeason(compSeasonEventKey, compSeasonEvent);
            output = "Participants successfully inserted!";
        }
        else
            output = "Already participants in event!";

        resp.getWriter().append(output);
    }
}
