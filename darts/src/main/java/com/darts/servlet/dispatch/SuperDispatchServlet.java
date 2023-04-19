package com.darts.servlet.dispatch;

import com.sports.entity.CompSeasonPhase;
import com.sports.entity.PersonMatch;
import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.PersonSportManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;

public abstract class SuperDispatchServlet extends com.sportservlet.dispatch.SuperDispatchServlet {
    void prepareMatch(Statement stat, HttpServletRequest req, PersonMatch personMatch) throws SQLException {
        Map<Integer, PersonSport> personMap = new PersonSportManager(stat).getParticipantMap(
                Arrays.asList(personMatch.getPersonSport1Id(), personMatch.getPersonSport2Id()));

        personMatch.setPerson1Name(personMap.get(personMatch.getPersonSport1Id()).getDescription());
        personMatch.setPerson2Name(personMap.get(personMatch.getPersonSport2Id()).getDescription());

        if (personMatch.getPerson1score() == null)
            personMatch.setPerson1score(0);

        if (personMatch.getPerson2score() == null)
            personMatch.setPerson2score(0);

        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(compSeasonKey, personMatch.getCompSeasonPhaseId());

        CompSeasonPhase csp = new CompSeasonPhaseManager(stat).getCompSeasonPhase(cspk);

        personMatch.setBestOf1(csp.getBestOf1());
        personMatch.setBestOf2(csp.getBestOf2());
        personMatch.setBestOfDec(csp.getBestOfDec());

        HttpSession session = req.getSession();
        session.setAttribute("pmk", new PersonMatchKey(compSeasonKey, personMatch.getPersonMatchId()));
        session.setAttribute("pm", personMatch);
    }
}
