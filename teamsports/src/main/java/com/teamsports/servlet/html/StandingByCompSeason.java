package com.teamsports.servlet.html;

import com.sports.logic.calculation.DbCalculation;
import com.teamsports.servlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class StandingByCompSeason extends SuperHtmlServlet {
    void initSpecificProperties(HttpServletRequest req) {
        jsList.add("standing");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        w.append("const oc = true;\n");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"init();\">\n");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<select id=\"pid\" onchange=\"handleSelectPhase();\"></select>\n");

        ServletUtil.writeStandingElements(w);
    }
}
