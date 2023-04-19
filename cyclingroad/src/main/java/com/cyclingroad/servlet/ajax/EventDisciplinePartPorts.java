package com.cyclingroad.servlet.ajax;

import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SportEventManager;
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
        int spid = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
        int eid = getIntValuedParameterValue(req, "eid");
        int csepid = getIntValuedParameterValue(req, "csepid");
        int edpid = getIntValuedParameterValue(req, "edpid");

        boolean isTeam = new SportEventManager(stat).getEntityFromSuperKey(new SportEventKey(spid, eid)).isTeam();

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
                "&eid=" + eid + "&csepid=" + csepid + "&edpid=" + edpid, "Manage " + participantAsString, w);
        w.append("<input type=\"button\" onclick=\"insertDisciplinePartParticipants();\" value=\"Insert ");
        w.append(participantAsString);
        w.append(" in discipline part\" /><br/>\n");
        w.append("<div id=\"divIns\"></div>\n");
    }
}
