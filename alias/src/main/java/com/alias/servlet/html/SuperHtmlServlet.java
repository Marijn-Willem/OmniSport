package com.alias.servlet.html;

import com.alias.servlet.util.ServletUtil;
import com.sports.logic.util.Util;
import com.sportservlet.html.AbstractHtmlServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SuperHtmlServlet extends com.sportservlet.html.SuperHtmlServlet implements AbstractHtmlServlet {
    abstract void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException;

    @Override
    protected void initProperties(HttpServletRequest req) {
        cssList.add("styling");
        jsList.add("general");
        jsSpecificList.add("alias");
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        if (req.getParameter("aeid") != null)
            writeVarInScriptTag("aeid", getIntValuedParameterValue(req, "aeid"), w);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Integer aeid = Util.convertStringToNegativeInteger(Util.convertNullStringToEmpty(req.getParameter("aeid")));
        ServletUtil.writeEntityList(aeid, res.getWriter());

        processSpecific(req, res);
    }

    String getEntityName(HttpServletRequest req) {
        int aeid = getIntValuedParameterValue(req, "aeid");
        return ServletUtil.entityMetaDataMap.get(aeid).getEntityName();
    }
}
