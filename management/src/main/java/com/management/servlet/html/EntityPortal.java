package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EntityPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        writeEntityLink("Client", res.getWriter());
        writeEntityLink("Language", res.getWriter());
        writeEntityLink("Competition", res.getWriter());
        writeEntityLink("Season", res.getWriter());
        writeEntityLink("Sport", res.getWriter());
        writeEntityLink("Geo", res.getWriter());
        writeEntityLink("Club", res.getWriter());
        writeEntityLink("Team", res.getWriter());
        writeEntityLink("Noc", res.getWriter());
        writeEntityLink("Equipe", res.getWriter());
        writeEntityLink("LocationRole", res.getWriter());
        writeEntityLink("PhaseType", res.getWriter());
        writeEntityLink("EventPartName", res.getWriter());
        writeEntityLink("NoCountResult", res.getWriter());
    }

    private void writeEntityLink(String entityName, Writer w) throws IOException {
        w.append("<div>\n<a href=\"");
        w.append(path);
        w.append("/");
        w.append(entityName);
        w.append("Portal\">");
        w.append(entityName);
        w.append("</a>\n</div>\n");
    }
}
