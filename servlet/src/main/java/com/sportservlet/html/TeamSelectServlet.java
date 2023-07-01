package com.sportservlet.html;

import com.sports.entity.Gender;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class TeamSelectServlet extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected abstract void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException;

    protected void initAbstractProperties() { }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("team");
        initAbstractProperties();
        initSpecificProperties(req);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"sportListLoader.loadElement();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        writeInitStateVarInScriptTag("spid", req, w);
        writeInitStateVarInScriptTag("gid", req, w);
        writeInitStateVarInScriptTag("tid", req, w);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<select id=\"spid\" onchange=\"teamListLoader.loadElement();\">\n</select><br/>\n");

        w.append("<select id=\"gid\" onchange=\"teamListLoader.loadElement();\">\n");
        LinkedHashMap<Integer, String> genderMap = Gender.getGenderLinkedHashMap();
        for (Map.Entry<Integer, String> me : genderMap.entrySet())
            ServletUtil.writeOption(me.getKey(), me.getValue(), w);
        w.append("</select><br/>\n");

        w.append("<select id=\"tid\">\n<select><br/>\n");
        processSpecific(req, res);
    }
}
