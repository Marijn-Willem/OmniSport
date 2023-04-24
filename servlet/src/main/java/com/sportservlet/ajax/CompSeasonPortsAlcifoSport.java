package com.sportservlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.Gender;
import com.sports.entity.SportEvent;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SportEventManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class CompSeasonPortsAlcifoSport extends SuperResponseServlet {
    protected List<SportEvent> sportEventList;

    protected abstract String getSpecificURLCells(int eventId);

    protected void processSpecific(Writer w) throws IOException { }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<CompSeasonEvent> compSeasonEvents = new CompSeasonEventManager(stat).getCompSeasonEvents(compSeasonKey);
        List<SportEventKey> sportEventKeys = new ArrayList<>() {{
            addAll(compSeasonEvents.stream().map(CompSeasonEvent::getSportEventKey).toList());
        }};

        sportEventList = new SportEventManager(stat).getSportEventListByKeys(sportEventKeys);

        Writer w = resp.getWriter();

        w.append("<table class=\"click_through\">\n");

        for (SportEvent sportEvent : sportEventList) {
            int eventId = sportEvent.getSportEventId();

            String url, title;

            if (sportEvent.isTeam()) {
                url = "EventTeamImport";
                title = "Import teams";
            }
            else {
                url = "EventPersonImport";
                title = "Import persons";
            }

            String line = "<tr><td>" + Util.concatStringsWithDelimiter(sportEvent.getName(),
                    Gender.getGenderNameFromId(sportEvent.getGenderId()), " - ") + "</td>" +
                    getSpecificURLCells(eventId) +
                    getURLCell(eventId, url, title) +
                    "</tr>\n";

            w.append(line);
        }

        w.append("</table>\n");
        processSpecific(w);
    }

    protected String getURLCell(int eventId, String url, String title) {
        String paramList = "cid=" + competitionId + "&sid=" + seasonId + "&eid=" + eventId;

        return "<td onclick=\"goToUrl('" + url + "', '" + paramList + "');\">" + title + "</td>";
    }
}
