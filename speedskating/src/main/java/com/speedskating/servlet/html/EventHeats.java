package com.speedskating.servlet.html;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.PersonSport;
import com.sports.entity.Sport;
import com.sports.entity.comparator.EventPartPersonSportOrderHeat;
import com.sports.entity.comparator.OrderableOrder;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.entity.manager.PersonSportManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EventHeats extends SuperHtmlServlet {
    @Override
    void initSpecificProperties(HttpServletRequest req) {
        jsList.add("eventheats");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonEventVarsInScriptTag(req, w);
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
        int compSeasonEventId = Integer.parseInt(req.getParameter("cseid"));

        CompSeasonEventKey csek = getCompSeasonEventKey(req);

        List<CompSeasonEventPart> compSeasonEventParts = new CompSeasonEventPartManager(stat)
                .getCompSeasonEventPartsFromEvents(Collections.singletonList(csek));

        List<EventPartPersonSport> eventPartPersonSports =
                new EventPartPersonSportManager(stat).getEventPartPersonSportListWithHeats(csek);

        List<Integer> personSportIds = new ArrayList<>();

        for (EventPartPersonSport eventPartPersonSport : eventPartPersonSports) {
            personSportIds.add(eventPartPersonSport.getPersonSportId());

            for (CompSeasonEventPart compSeasonEventPart : compSeasonEventParts)
                if (compSeasonEventPart.getCompSeasonEventPartId() == eventPartPersonSport.getCompSeasonEventPartId()) {
                    eventPartPersonSport.setSportEventPartOrder(compSeasonEventPart.getOrder());
                    break;
                }
        }

        new DbCalculation(stat).setCompSeasonEventPartDescriptions(csek, compSeasonEventParts);
        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(personSportIds);

        compSeasonEventParts.sort(new OrderableOrder());
        eventPartPersonSports.sort(new EventPartPersonSportOrderHeat());

        Writer w = res.getWriter();
        String line;

        int eventPersonIndX = 0;

        for (CompSeasonEventPart compSeasonEventPart : compSeasonEventParts) {
            line = "<h3>" + compSeasonEventPart.getDescription() + "</h3>\n";
            w.append(line);
            w.append("<table border=\"1\">\n");

            int compSeasonEventPartId = compSeasonEventPart.getCompSeasonEventPartId();

            while (eventPersonIndX < eventPartPersonSports.size() &&
                    eventPartPersonSports.get(eventPersonIndX).getCompSeasonEventPartId() == compSeasonEventPartId) {
                int person1Id = eventPartPersonSports.get(eventPersonIndX).getPersonSportId();
                int person2Id = eventPartPersonSports.get(eventPersonIndX + 1).getPersonSportId();

                line = "<tr><td>" + personSportMap.get(person1Id).getDescription() + "</td><td>" +
                        personSportMap.get(person2Id).getDescription() + "</td><td>" +
                        "<a href=\"" + path + "/HeatOverview?cid=" + competitionId + "&sid=" + seasonId +
                        "&cseid=" + compSeasonEventId + "&csepid=" + compSeasonEventPartId + "&p1id=" + person1Id +
                        "&p2id=" + person2Id + "\">Overview</a></td></tr>\n";

                w.append(line);
                eventPersonIndX += 2;
            }

            w.append("</table>\n");

            line = "<div><input type=\"button\" onclick=\"insertRanking('/TotalRanking', " +
                    compSeasonEventPartId + ");\" value=\"Total rank\" />";

            w.append(line);

            line = "<input type=\"button\" onclick=\"insertRanking('/EventPartRanking', " +
                    compSeasonEventPartId + ");\" value=\"Distance rank\" /></div>\n";

            w.append(line);

            line = "<table id=\"tbl_rnk_" + compSeasonEventPartId + "\">\n</table>\n";

            w.append(line);

            line = "<div><a href=\"" + path + "/TimeHeat?cid=" + competitionId + "&sid=" + seasonId +
                    "&cseid=" + compSeasonEventId + "&csepid=" + compSeasonEventPartId + "\">Add Heat</a>\n<br/>\n";
            w.append(line);
            line = "<a href=\"" + path + "/AddEventPartPersonSports?cid=" + competitionId + "&sid=" + seasonId +
                    "&cseid=" + compSeasonEventId + "&csepid=" + compSeasonEventPartId + "\">Add Persons</a></div>\n";
            w.append(line);
        }
    }
}
