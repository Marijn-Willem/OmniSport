package com.cyclingroad.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class Scrape extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("scrape");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonEventPartVarsInScriptTag(req, w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonEventPartPortal?" + compSeasonUrlParameters +
                "&cseid=" + getIntValuedParameterValue(req, "cseid") +
                "&csepid=" + getIntValuedParameterValue(req, "csepid");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<span>Insert HTML source: <input id=\"divInp\" type=\"text\" /></span><br/>\n");
        w.append("<input type=\"button\" onclick=\"processScrape();\" value=\"Submit\" /><br/>\n");
        w.append("<div id=\"divRes\" type=\"text\"></div><br/>\n");
    }
}
