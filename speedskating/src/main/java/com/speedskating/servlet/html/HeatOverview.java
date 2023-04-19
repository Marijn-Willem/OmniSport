package com.speedskating.servlet.html;

import com.sports.calc.alcifo.Calculation;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.DisciplinePartPersonSport;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.PersonSport;
import com.sports.entity.Sport;
import com.sports.entity.comparator.EventDisciplinePartOrder;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartPersonSportKey;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.DisciplinePartPersonSportManager;
import com.sports.entity.manager.EventDisciplinePartManager;
import com.sports.entity.manager.PersonSportManager;
import com.sports.logic.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class HeatOverview extends SuperHtmlServlet {
    @Override
    void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");
        return "EventHeats?cid=" + competitionId + "&sid=" + seasonId + "&eid=" + eid;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        int eventId = Integer.parseInt(req.getParameter("eid"));
        int eventPartId = Integer.parseInt(req.getParameter("epid"));
        int ps1Id = Integer.parseInt(req.getParameter("p1id"));
        int ps2Id = Integer.parseInt(req.getParameter("p2id"));

        CompSeasonEventPartKey csepk = new CompSeasonEventPartKey(
                new CompSeasonEventKey(compSeasonKey, Sport.sportIdSpeedSkating, eventId),
                eventPartId);

        DbCalculation dbCalc = new DbCalculation(stat);

        SportDisciplineKey sdk = dbCalc.getSportDisciplineKey(csepk);

        EventPartPersonSportKey eppk1 = new EventPartPersonSportKey(csepk, ps1Id);
        EventPartPersonSportKey eppk2 = new EventPartPersonSportKey(csepk, ps2Id);

        List<DisciplinePartPersonSport> disciplinePartPersonSports =
                new DisciplinePartPersonSportManager(stat).getDisciplinePartPersonSportList(Arrays.asList(eppk1, eppk2));

        List<DisciplinePartPersonSport> dpParts1 = new ArrayList<>();
        List<DisciplinePartPersonSport> dpParts2 = new ArrayList<>();

        for (DisciplinePartPersonSport disciplinePartPersonSport : disciplinePartPersonSports)
            if (disciplinePartPersonSport.getPersonSportId() == ps1Id)
                dpParts1.add(disciplinePartPersonSport);
            else
                dpParts2.add(disciplinePartPersonSport);

        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(Arrays.asList(ps1Id, ps2Id));

        List<EventDisciplinePart> eventDisciplineParts = new EventDisciplinePartManager(stat).getEventDisciplineList(csepk);
        dbCalc.fillDisciplinePartsForEventDisciplineParts(sdk, eventDisciplineParts);
        eventDisciplineParts.sort(new EventDisciplinePartOrder());

        dpParts1 = Calculation.getDisciplinePartPersonsSorted(dpParts1, eventDisciplineParts);
        dpParts2 = Calculation.getDisciplinePartPersonsSorted(dpParts2, eventDisciplineParts);

        Writer w = res.getWriter();

        w.append("<table border=\"1\">\n");

        String person1Name = personSportMap.get(ps1Id).getDescription();
        String person2Name = personSportMap.get(ps2Id).getDescription();

        String line = "<tr><th colspan=\"2\">" + person1Name + "</th><th/>" +
                "<th colspan=\"2\">" + person2Name + "</th></tr>\n";
        w.append(line);

        w.append("<tr><th>Interm</th><th>Lap</th><th>Distance</th><th>Lap</th><th>Interm</th></tr>\n");

        int ms1_prev = 0, ms2_prev = 0;

        for (int i = 0; i < eventDisciplineParts.size(); i++) {
            String distance = eventDisciplineParts.get(i).getDisciplinePart().getName();
            int ms1 = dpParts1.get(i).getPoints();
            int ms2 = dpParts2.get(i).getPoints();

            line = "<tr><td>" + Util.getTimeStringFromMillis(ms1) + "</td><td>" +
                    Util.getTimeStringFromMillis(ms1 - ms1_prev) + "</td><td>" +
                    distance + "</td><td>" +
                    Util.getTimeStringFromMillis(ms2 - ms2_prev) + "</td><td>" +
                    Util.getTimeStringFromMillis(ms2) + "</td></tr>\n";

            w.append(line);

            ms1_prev = ms1;
            ms2_prev = ms2;
        }

        w.append("</table>\n");
    }
}
