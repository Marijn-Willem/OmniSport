package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class CompDivisionPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compdivision");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadCompDivisionList();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeVarInScriptTag("cid", competitionId, w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompetitionPortal?cid=" + competitionId;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"did\"></select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageCompDivision();\" value=\"Manage Competition Division\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddCompDivision();\" value=\"Add Competition Division\" /><br/>\n");
    }
}
