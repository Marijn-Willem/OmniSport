package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EntityInstancePortal extends SuperHtmlServlet {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return getEntityName(req) + "Portal?aeid=" + getIntValuedParameterValue(req, "aeid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        super.processScriptTag(stat, req, w);
        writeVarInScriptTag("eid", getEntityId(req), w);
        w.append("enm = '");
        w.append(getEntityName(req));
        w.append("';\n");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadEntityInstanceList();\">\n");
    }

    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"eiid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAliasPortal(getEntityInstanceKey);\" ");
        w.append("value=\"Manage Aliases\" /><br/>\n");
    }

    private Integer getEntityId(HttpServletRequest req) {
        String eidReq = req.getParameter("eid");

        if (eidReq == null)
            return  (Integer) req.getAttribute("eid");

        return Integer.parseInt(eidReq);
    }
}
