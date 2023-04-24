package com.cyclingroad.servlet.ajax;

import com.sports.calc.cyclingroad.Calculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.Sport;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SportEventManager;
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
        int eid = getIntValuedParameterValue(req, "eid");
        int csepid = getIntValuedParameterValue(req, "csepid");

        String parameters = compSeasonUrlParameters + "&eid=" + eid + "&csepid=" + csepid;

        boolean isTeam = new SportEventManager(stat).getEntityFromSuperKey(
                new SportEventKey(Sport.sportIdCyclingRoad, eid)).isTeam();

        boolean showSetGeneralPoints = Calculation.isGeneralClassification(eid);

        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(
                getCompSeasonEventKey(req));

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
        if (compSeasonEvent.getExternalSource() != null)
            w.append("<input type=\"button\" onclick=\"scrape();\" value=\"Scrape\" /><br/>\n");
        w.append("<div id=\"divIns\"></div>\n");
    }
}
