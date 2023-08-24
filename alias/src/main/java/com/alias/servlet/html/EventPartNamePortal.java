package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EventPartNamePortal extends SuperHtmlServlet {
    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadEventPartNameList();\">\n");
    }

    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"epnid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAliasPortal(getEventPartNameId);\" value=\"Manage Aliases\" /><br/>\n");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }
}
