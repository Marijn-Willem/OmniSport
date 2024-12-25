package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class ClientPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("client");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeInitStateVarInScriptTag("cnid", req, w);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadClientList();\">\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"selCnid\"></select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageClient();\" value=\"Manage Client\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddClient();\" value=\"Add Client\" /><br/>\n");
    }
}
