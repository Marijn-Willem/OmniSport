package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class SportEventPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sportevent");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"sportEventListLoader.loadElement();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeVarInScriptTag("spid", getIntValuedParameterValue(req, "spid"), w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "SportPortal?spid=" + getIntValuedParameterValue(req, "spid");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        int spid = Integer.parseInt(req.getParameter("spid"));

        w.append("<select id=\"eid\" onchange=\"handleChangeSportEvent();\">\n</select>\n<br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageSportEvent();\" value=\"Manage Sport Event\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToSportEventPartPortal();\" value=\"Manage Sport Event Parts\" /><br/>\n");
        w.append("<input id=\"btnEpn\" type=\"button\" onclick=\"goToEventPartNamePortal();\" value=\"Manage Event Part Names\" /><br/>\n");
        w.append("<div>\n<a href=\"");
        w.append(path);
        w.append("/ManageSportEvent?spid=");
        w.append(Integer.toString(spid));
        w.append("&md=i\">Add Sport Event</a>\n</div>\n");
    }
}
