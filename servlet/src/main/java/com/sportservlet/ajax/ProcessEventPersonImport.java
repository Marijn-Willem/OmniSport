package com.sportservlet.ajax;

import com.sports.calc.alcifo.Calculation;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.entity.manager.EventPersonSportManager;
import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class ProcessEventPersonImport extends ProcessPersonImport {
    private CompSeasonEventKey csek;
    private EventPersonSportManager eventPersonSportManager;
    private boolean insertEventPartPersonSportWithRank;
    private boolean insertEventPartPersonSportForSinglePartEvent;
    private CompSeasonEventPartKey csepk;
    private EventPartPersonSportManager eventPartPersonSportManager;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {
        int sportId = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
        int eventId = Integer.parseInt(req.getParameter("eid"));

        csek = new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId), sportId, eventId);

        eventPersonSportManager = new EventPersonSportManager(stat);

        insertEventPartPersonSportWithRank = Calculation.isCyclingRoadSingleRace(new SportEventKey(sportId, eventId));

        insertEventPartPersonSportForSinglePartEvent = insertEventPartPersonSportWithRank ||
                sportId == Sport.sportIdSpeedSkating;

        csepk = null;
        eventPartPersonSportManager = null;

        if (insertEventPartPersonSportForSinglePartEvent) {
           List<CompSeasonEventPartKey> eventPartKeys = new CompSeasonEventPartManager(stat).getCompSeasonEventParts(csek);
           if (eventPartKeys.size() == 1) {
               csepk = eventPartKeys.get(0);
               eventPartPersonSportManager = new EventPartPersonSportManager(stat);
           }
        }
    }

    protected List<Integer> getParticipantIds() throws SQLException {
        return eventPersonSportManager.getPersonSportIdsCompSeasonEvent(csek);
    }

    protected void processPersonSport(int personSportId, int nameIndX) throws SQLException {
        EventPersonSportKey eventPersonSportKey = new EventPersonSportKey(csek, personSportId);
        eventPersonSportManager.insertEventPersonSport(eventPersonSportKey, new EventPersonSport());
        if (insertEventPartPersonSportForSinglePartEvent)
            insertEventPartPersonSport(personSportId, nameIndX);
    }

    protected Map<String, Person> getPersonNameMap(Statement stat, List<String> names) throws SQLException {
        return new DbCalculation(stat).getPersonNameMapWithNewPersons(names, csek.getSportEventKey());
    }

    private void insertEventPartPersonSport(int personSportId, int nameIndX) throws SQLException {
        if (csepk != null && eventPartPersonSportManager != null) {
            EventPartPersonSportKey eventPartPersonSportKey = new EventPartPersonSportKey(csepk, personSportId);
            EventPartPersonSport eventPartPersonSport = new EventPartPersonSport();
            if (insertEventPartPersonSportWithRank)
                eventPartPersonSport.setRank(nameIndX + 1);

            eventPartPersonSportManager.insertEventPartPersonSport(eventPartPersonSportKey, eventPartPersonSport);
        }
    }
}
