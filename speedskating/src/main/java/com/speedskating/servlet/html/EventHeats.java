package com.speedskating.servlet.html;

import com.sports.entity.*;
import com.sports.entity.comparator.EventPartPersonSportOrderHeat;
import com.sports.entity.comparator.SportEventPartOrder;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.entity.manager.PersonSportManager;
import com.sports.entity.manager.SportEventPartManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class EventHeats extends SuperHtmlServlet {
    @Override
    void initSpecificProperties(HttpServletRequest req) {
        jsList.add("eventheats");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int eventId = Integer.parseInt(req.getParameter("eid"));

        String rule = "const cid=" + competitionId + ";\nconst sid=" + seasonId +
                ";\nconst eid=" + eventId + ";\n";

        w.append(rule);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPortal?spid=" + Sport.sportIdSpeedSkating + "&" + compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int eventId = Integer.parseInt(req.getParameter("eid"));

        CompSeasonEventKey csek = new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId), eventId);
        SportEventKey sek = new SportEventKey(Sport.sportIdSpeedSkating, eventId);

        List<SportEventPart> sportEventParts = new SportEventPartManager(stat).getSportEventParts(sek);
        List<EventPartPersonSport> eventPartPersonSports =
                new EventPartPersonSportManager(stat).getEventPartPersonSportListWithHeats(csek);

        List<Integer> personSportIds = new ArrayList<>();

        for (EventPartPersonSport eventPartPersonSport : eventPartPersonSports) {
            personSportIds.add(eventPartPersonSport.getPersonSportId());

            for (SportEventPart sportEventPart : sportEventParts)
                if (sportEventPart.getSportEventPartId() == eventPartPersonSport.getCompSeasonEventPartId()) {
                    eventPartPersonSport.setSportEventPartOrder(sportEventPart.getOrder());
                    break;
                }
        }

        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(personSportIds);

        sportEventParts.sort(new SportEventPartOrder());
        eventPartPersonSports.sort(new EventPartPersonSportOrderHeat());

        Writer w = res.getWriter();
        String line;

        int eventPersonIndX = 0;

        for (SportEventPart sportEventPart : sportEventParts) {
            line = "<h3>" + sportEventPart.getName() + "</h3>\n";
            w.append(line);
            w.append("<table border=\"1\">\n");

            int sportEventPartId = sportEventPart.getSportEventPartId();

            while (eventPersonIndX < eventPartPersonSports.size() &&
                    eventPartPersonSports.get(eventPersonIndX).getCompSeasonEventPartId() == sportEventPartId) {
                int person1Id = eventPartPersonSports.get(eventPersonIndX).getPersonSportId();
                int person2Id = eventPartPersonSports.get(eventPersonIndX + 1).getPersonSportId();

                line = "<tr><td>" + personSportMap.get(person1Id).getDescription() + "</td><td>" +
                        personSportMap.get(person2Id).getDescription() + "</td><td>" +
                        "<a href=\"" + path + "/HeatOverview?cid=" + competitionId + "&sid=" + seasonId +
                        "&eid=" + eventId + "&epid=" + sportEventPartId + "&p1id=" + person1Id +
                        "&p2id=" + person2Id + "\">Overview</a></td></tr>\n";

                w.append(line);
                eventPersonIndX += 2;
            }

            w.append("</table>\n");

            line = "<div><input type=\"button\" onclick=\"insertRanking('/TotalRanking', " +
                    sportEventPart.getSportEventPartId() + ");\" value=\"Total rank\" />";

            w.append(line);

            line = "<input type=\"button\" onclick=\"insertRanking('/EventPartRanking', " +
                    sportEventPart.getSportEventPartId() + ");\" value=\"Distance rank\" /></div>\n";

            w.append(line);

            line = "<table id=\"tbl_rnk_" + sportEventPart.getSportEventPartId() + "\">\n</table>\n";

            w.append(line);

            line = "<div><a href=\"" + path + "/TimeHeat?cid=" + competitionId + "&sid=" + seasonId +
                    "&eid=" + eventId + "&epid=" + sportEventPartId + "\">Add Heat</a>\n<br/>\n";
            w.append(line);
            line = "<a href=\"" + path + "/AddEventPartPersonSports?cid=" + competitionId + "&sid=" + seasonId +
                    "&eid=" + eventId + "&epid=" + sportEventPartId + "\">Add Persons</a></div>\n";
            w.append(line);
        }
    }
}
