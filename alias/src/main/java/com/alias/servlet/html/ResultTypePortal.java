package com.alias.servlet.html;

import com.sports.entity.ResultType;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

public class ResultTypePortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        TreeMap<Integer, String> resultTypeMap = new TreeMap<>(ResultType.resultTypeLinkedHashMap);

        Writer w = res.getWriter();

        w.append("<select id=\"rtid\">\n");

        for (Map.Entry<Integer, String> me : resultTypeMap.entrySet())
            ServletUtil.writeOption(me.getKey(), me.getValue(), w);

        w.append("</select><br/>\n");

        w.append("<input type=\"button\" onclick=\"goToAliasPortal(getResultTypeId);\" value=\"Manage Aliases\" /><br/>\n");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }
}
