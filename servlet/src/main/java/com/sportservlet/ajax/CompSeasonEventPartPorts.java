package com.sportservlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class CompSeasonEventPartPorts extends SuperResponseServlet {
    private String partUrl, participantAsString;
    protected String parameters;

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        int cseid = getIntValuedParameterValue(req, "cseid");
        int csepid = getIntValuedParameterValue(req, "csepid");

        parameters = compSeasonUrlParameters + "&cseid=" + cseid + "&csepid=" + csepid;

        boolean isTeam = new DbCalculation(stat).getSportEvent(getCompSeasonEventKey(req)).isTeam();

        if (isTeam) {
            partUrl = "ManageEventPartTeams";
            participantAsString = "teams";
        }
        else {
            partUrl = "ManageEventPartPersonSports";
            participantAsString = "person sports";
        }
    }

    protected void writeManageParticipantButton(Writer w) throws IOException {
        ServletUtil.writeGenericGoToButton(partUrl, parameters, "Manage " + participantAsString, w);
    }

    protected void writeInsertParticipantButton(Writer w) throws IOException {
        w.append("<input type=\"button\" onclick=\"insertEventPartParticipants();\" value=\"Insert ");
        w.append(participantAsString);
        w.append(" in event part\" /><br/>\n");
    }
}
