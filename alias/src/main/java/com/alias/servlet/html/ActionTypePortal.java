package com.alias.servlet.html;

import com.sports.entity.ActionType;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

public class ActionTypePortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, Integer> nameIdSortedMap = new TreeMap<>(ActionType.nameIdMap);

        Writer w = res.getWriter();

        w.append("<select id=\"atid\">\n");

        for (Map.Entry<String, Integer> me : nameIdSortedMap.entrySet())
            ServletUtil.writeOption(me.getValue(), me.getKey(), w);

        w.append("</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAliasPortal(getActionTypeId);\" value=\"Manage Aliases\" /><br/>\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }
}
