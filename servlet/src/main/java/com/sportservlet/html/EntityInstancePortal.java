package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public abstract class EntityInstancePortal extends SuperHtmlServlet implements AbstractHtmlServlet {
    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("entity");
        jsList.add("entityinstance");
        initSpecificProperties(req);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadEntityInstanceList();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeVarInScriptTag("eid", getEntityId(req), w);
        w.append("const enm = '");
        w.append(req.getParameter("enm"));
        w.append("';\n");
        writeInitStateVarInScriptTag("eiid", req, w);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"eiid\">\n</select><br/>\n");
        w.append("<span>Date: <input id=\"dt\" type=\"text\" /></span><br/>\n");
        w.append("<input type=\"button\" onclick=\"insertEntityInstance();\" value=\"Insert new instance\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageEntityInstance();\" value=\"Manage instance\" /><br/>\n");
        w.append("<div id=\"divIns\"></div>\n");
    }

    private int getEntityId(HttpServletRequest req) {
        Integer eidAtt = (Integer)req.getAttribute("eid");

        return eidAtt != null ? eidAtt : getIntValuedParameterValue(req, "eid");
    }
}
