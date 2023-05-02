package com.cyclingroad.servlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.SportEvent;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonEventPartPorts extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int cseid = getIntValuedParameterValue(req, "cseid");
        int csepid = getIntValuedParameterValue(req, "csepid");

        String parameters = compSeasonUrlParameters + "&cseid=" + cseid + "&csepid=" + csepid;

        CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(getCompSeasonEventKey(req));
        int sportEventId = cse.getSportEventKey().getSportEventId();

        boolean isTeam = new DbCalculation(stat).getSportEvent(getCompSeasonEventKey(req)).isTeam();
        boolean showSetGeneralPoints = sportEventId == SportEvent.sportEventIdCyclingRoadGeneral;

        String partUrl, participantAsString;

        if (isTeam) {
            partUrl = "ManageEventPartTeams";
            participantAsString = "teams";
        }
        else {
            partUrl = "ManageEventPartPersonSports";
            participantAsString = "person sports";
        }

        Writer w = resp.getWriter();

        ServletUtil.writeGenericGoToButton(partUrl, parameters, "Manage " + participantAsString, w);
        ServletUtil.writeGenericGoToButton("EventDisciplinePartPortal", parameters, "Manage discipline parts", w);
        w.append("<input type=\"button\" onclick=\"insertEventPartParticipants();\" value=\"Insert ");
        w.append(participantAsString);
        w.append(" in event part\" /><br/>\n");
        if (showSetGeneralPoints) {
            w.append("<input type=\"button\" onclick=\"setGeneralClassificationPoints();\" ");
            w.append("value=\"Calculate general classification times\" /><br/>\n");
        }
        if (cse.getExternalSource() != null)
            w.append("<input type=\"button\" onclick=\"scrape();\" value=\"Scrape\" /><br/>\n");
        w.append("<div id=\"divIns\"></div>\n");
    }
}
