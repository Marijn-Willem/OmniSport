package com.cyclingroad.servlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class EventDisciplinePartPorts extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int cseid = getIntValuedParameterValue(req, "cseid");
        int csepid = getIntValuedParameterValue(req, "csepid");
        int edpid = getIntValuedParameterValue(req, "edpid");

        boolean isTeam = new DbCalculation(stat).getSportEvent(getCompSeasonEventKey(req)).isTeam();

        String partUrl, participantAsString;

        if (isTeam) {
            partUrl = "ManageDisciplinePartTeams";
            participantAsString = "teams";
        }
        else {
            partUrl = "ManageDisciplinePartPersonSports";
            participantAsString = "person sports";
        }

        Writer w = resp.getWriter();

        ServletUtil.writeGenericGoToButton(partUrl, compSeasonUrlParameters +
                "&cseid=" + cseid + "&csepid=" + csepid + "&edpid=" + edpid, "Manage " + participantAsString, w);
        w.append("<input type=\"button\" onclick=\"insertDisciplinePartParticipants();\" value=\"Insert ");
        w.append(participantAsString);
        w.append(" in discipline part\" /><br/>\n");
        w.append("<div id=\"divIns\"></div>\n");
    }
}
