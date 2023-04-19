package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class ClubPortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<input id=\"nm\" type=\"text\" onkeypress=\"clubNameListLoader.loadElement();\" /><br/>\n");
        w.append("<table id=\"tblNm\" border=\"1\">\n</table>\n");
        w.append("<input type=\"button\" onclick=\"loadClubIdByName();\" value=\"Manage Aliases\" /><br/>\n");
        w.append("<input id=\"clid\" type=\"hidden\" /><br/>\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("club");
    }
}
