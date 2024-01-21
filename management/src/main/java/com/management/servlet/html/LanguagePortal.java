package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class LanguagePortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("language");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeInitStateVarInScriptTag("lid", req, w);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadLanguageList();\">\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"selLid\"></select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageLanguage();\" value=\"Manage Language\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddLanguage();\" value=\"Add Language\" /><br/>\n");
    }
}
