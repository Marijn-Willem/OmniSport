package com.speedskating.servlet.html;

import com.sports.entity.PersonSport;
import com.sports.entity.Sport;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.entity.manager.EventPersonSportManager;
import com.sports.entity.manager.PersonSportManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AddEventPartPersonSports extends SuperHtmlServlet {
    void initSpecificProperties(HttpServletRequest req) {
        jsList.add("eventpartpersonsport");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");
        return "EventHeats?cid=" + competitionId + "&sid=" + seasonId + "&eid=" + eid;
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int eventId = Integer.parseInt(req.getParameter("eid"));
        int eventPartId = Integer.parseInt(req.getParameter("epid"));

        CompSeasonEventKey csek = new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId),
                            Sport.sportIdSpeedSkating, eventId);

        CompSeasonEventPartKey csepk = new CompSeasonEventPartKey(csek, eventPartId);

        List<Integer> psIdsCse = new EventPersonSportManager(stat).getPersonSportIdsCompSeasonEvent(csek);
        List<Integer> psIdsCsep = new EventPartPersonSportManager(stat).getPersonSportIdsCompSeasonEventPart(csepk);

        List<PersonSport> personSports = new PersonSportManager(stat).getParticipantList(psIdsCse);

        Writer w = res.getWriter();

        String line = "<form action=\"" + path + "/ProcessAddEventPartPersonSports\">\n";

        w.append(line);
        w.append("<input id=\"cb_all\" type=\"checkbox\" onchange=\"checkAll();\" />Select all<br/>\n<br/>\n");

        for (PersonSport personSport : personSports)
            if (!psIdsCsep.contains(personSport.getId())) {
                line = "<input name=\"pid\" type=\"checkbox\" value=\"" + personSport.getId() + "\" />" +
                    personSport.getDescription() + "<br/>\n";

                w.append(line);
            }

        w.append("<input type=\"submit\" value=\"Submit!\" /><br/>\n");

        line = "<input name=\"cid\" type=\"hidden\" value=\"" + competitionId + "\" /><br/>\n";
        w.append(line);
        line = "<input name=\"sid\" type=\"hidden\" value=\"" + seasonId + "\" /><br/>\n";
        w.append(line);
        line = "<input name=\"eid\" type=\"hidden\" value=\"" + eventId + "\" /><br/>\n";
        w.append(line);
        line = "<input name=\"epid\" type=\"hidden\" value=\"" + eventPartId + "\" /><br/>\n";
        w.append(line);

        w.append("</form><br/>\n");
    }
}
