package com.alias.servlet.html;

import com.sports.entity.StatType;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

public class StatTypePortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();
        Map<String, Integer> sortedNameIdMap = new TreeMap<>(StatType.nameIdMap);

        w.append("<select id=\"stid\">\n");

        for (Map.Entry<String, Integer> me : sortedNameIdMap.entrySet())
            ServletUtil.writeOption(me.getValue(), me.getKey(), w);

        w.append("</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAliasPortal(getStatTypeId);\" value=\"Manage Aliases\" /><br/>\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }
}
