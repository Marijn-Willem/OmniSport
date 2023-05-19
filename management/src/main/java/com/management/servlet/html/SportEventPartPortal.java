package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class SportEventPartPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sporteventpart");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"sportEventPartLoader.loadElement();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        w.append("const spid = ");
        w.append(req.getParameter("spid"));
        w.append(";\nconst eid = ");
        w.append(req.getParameter("eid"));
        w.append(";\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "SportEventPortal?spid=" + getIntValuedParameterValue(req, "spid") + "&eid=" +
                getIntValuedParameterValue(req, "eid");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");

        w.append("<select id=\"epid\">\n</select>\n<br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageSportEventPart();\" value=\"Manage Sport Event Part\" />\n");
        w.append("<div>\n<a href=\"");
        w.append(path);
        w.append("/ManageSportEventPart?spid=");
        w.append(Integer.toString(spid));
        w.append("&eid=");
        w.append(Integer.toString(eid));
        w.append("&md=i\">Add Sport Event Part</a>\n</div>\n");
    }
}
