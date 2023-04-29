package com.speedskating.servlet.ajax;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.Calculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.Participant;
import com.sports.entity.SportEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SportEventManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventPartRanking extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEventKey cseKey = getCompSeasonEventKey(req);
        CompSeasonEventPartKey csepk = getCompSeasonEventPartKey(req);

        CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);
        SportEvent sportEvent = new SportEventManager(stat).getEntityFromSuperKey(cse.getSportEventKey());

        AlcifoPartParticipantFactory factory = Calculation.getAlcifoParticipantFactory(sportEvent)
                .getEventPartParticipantFactory();

        List<? extends Participant> ranking = new com.sports.calc.alcifo.DbCalculation(stat)
                .getFullRankingInPart(factory, csepk);

        for (int i = 0; i < ranking.size() && ranking.get(i).getRank() != null; i++) {
            Participant participant = ranking.get(i);

            String line = "<tr><td>" + participant.getRank() + ".</td><td>" +
                    participant.getDescription() +
                    "</td><td>" + Util.getTimeStringFromMillis(participant.getPoints()) + "</td></tr>\n";

            resp.getWriter().append(line);
        }
    }
}
