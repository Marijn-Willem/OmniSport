package com.sportservlet.ajax;

import com.sports.calc.alcifo.Calculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.Gender;
import com.sports.entity.SportEvent;
import com.sports.entity.comparator.CompSeasonEventNameGenderId;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CompSeasonPortsAlcifoSport extends SuperResponseServlet {
    protected abstract String getSpecificURLCells(CompSeasonEvent compSeasonEvent, boolean isTeam);

    protected void processSpecific(Writer w) throws IOException { }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<CompSeasonEvent> compSeasonEvents = new CompSeasonEventManager(stat).getCompSeasonEvents(compSeasonKey);
        List<SportEventKey> sportEventKeys = new ArrayList<>() {{
            addAll(compSeasonEvents.stream().map(CompSeasonEvent::getSportEventKey).toList());
        }};

        Map<SportEventKey, SportEvent> sportEventMap = new HashMap<>() {{
            new SportEventManager(stat).getSportEventListByKeys(sportEventKeys).forEach(x -> {
                SportEventKey sek = new SportEventKey(x.getSportId(), x.getSportEventId());
                put(sek, x);
            });
        }};

        Calculation.setSportEventNames(compSeasonEvents, sportEventMap.values().stream().toList());
        compSeasonEvents.sort(new CompSeasonEventNameGenderId());

        Writer w = resp.getWriter();

        w.append("<table class=\"click_through\">\n");

        for (CompSeasonEvent compSeasonEvent : compSeasonEvents) {
            String url, title;
            boolean isTeam = sportEventMap.get(compSeasonEvent.getSportEventKey()).isTeam();

            if (isTeam) {
                url = "EventTeamImport";
                title = "Import teams";
            }
            else {
                url = "EventPersonImport";
                title = "Import persons";
            }

            int compSeasonEventId = compSeasonEvent.getCompSeasonEventId();

            String line = "<tr><td>" + Util.concatStringsWithDelimiter(compSeasonEvent.getSportEventName(),
                    Gender.getGenderNameFromId(compSeasonEvent.getGenderId()), " - ") + "</td>" +
                    getSpecificURLCells(compSeasonEvent, isTeam) +
                    getURLCell(compSeasonEventId, url, title) +
                    "</tr>\n";

            w.append(line);
        }

        w.append("</table>\n");
        processSpecific(w);
    }

    protected String getURLCell(int compSeasonEventId, String url, String title) {
        String paramList = "cid=" + competitionId + "&sid=" + seasonId + "&cseid=" + compSeasonEventId;

        return "<td onclick=\"goToUrl('" + url + "', '" + paramList + "');\">" + title + "</td>";
    }
}
