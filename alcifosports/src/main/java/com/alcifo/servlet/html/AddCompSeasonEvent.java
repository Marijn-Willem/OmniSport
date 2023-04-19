package com.alcifo.servlet.html;

import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class AddCompSeasonEvent extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("compseasonevent");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initSportEventList('SportEventListNotInCompSeason');\">\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Writer w = res.getWriter();

        w.append("<div>\n");
        w.append("<select id=\"selEid\">\n</select>\n");
        w.append("</div><br/>\n");
        w.append("<input type=\"button\" value=\"Add\" onclick=\"handleAddCompSeasonEvent();\" /><br/>\n");
        w.append("<div id=\"divAdd\"></div>\n");
    }
}
