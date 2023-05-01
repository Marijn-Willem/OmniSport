package com.alcifo.servlet.html;

import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonEventPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("compseasonevent");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initSportEventList();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeInitStateVarInScriptTag("cseid", req, w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Writer w = res.getWriter();

        w.append("<div>\n<select id=\"selCseid\">\n</select>\n</div><br/>\n");
        w.append("<input id=\"btnIns\" type=\"button\" value=\"Add Event Part\" ");
        w.append("onclick=\"goToInsertCompSeasonEventPart();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Manage Event\" ");
        w.append("onclick=\"goToManageCompSeasonEvent();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Go to Event Parts\" ");
        w.append("onclick=\"goToCompSeasonEventPartPortal();\" /><br/>\n");
    }
}
