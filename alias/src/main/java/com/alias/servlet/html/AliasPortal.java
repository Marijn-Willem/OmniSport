package com.alias.servlet.html;

import com.alias.servlet.util.EntityMetaData;
import com.alias.servlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class AliasPortal extends SuperHtmlServlet {
    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadAliasList();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        super.processScriptTag(stat, req, w);
        w.append("eid = '");
        w.append(req.getParameter("eid"));
        w.append("';\n");
    }

    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"aid\"></select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageAlias();\" value=\"Manage Alias\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddAlias();\" value=\"Add Alias\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"deleteAlias();\" value=\"Delete Alias\" /><br/>\n");
        w.append("<div id=\"divDel\"></div>\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        int aeid = getAeid(req);
        EntityMetaData emd = ServletUtil.entityMetaDataMap.get(aeid);

        String returnPath;

        if (emd.isHasInstance()) {
            String eid = req.getParameter("eid").split("_")[0];
            returnPath = "EntityInstancePortal?aeid=" + aeid + "&eid=" + eid;
        }
        else
            returnPath = getEntityName(req) + "Portal?aeid=" + aeid;

        return returnPath;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    private int getAeid(HttpServletRequest req) {
        return getIntValuedParameterValue(req, "aeid");
    }
}
