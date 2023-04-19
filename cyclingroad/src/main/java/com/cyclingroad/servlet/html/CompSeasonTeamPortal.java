package com.cyclingroad.servlet.html;

import com.sports.entity.Sport;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class CompSeasonTeamPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonteam");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadCompSeasonTeams();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPortal?spid=" + Sport.sportIdCyclingRoad + "&" + compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"tid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageCompSeasonTeamPersonSports();\" value=\"");
        w.append("Manage persons in team\" /><br/>\n");
    }
}
