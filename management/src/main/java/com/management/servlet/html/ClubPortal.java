package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ClubPortal extends SuperHtmlServlet {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("club");
        jsSpecificList.add("club");
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<input id=\"nm\" type=\"text\" onkeypress=\"clubNameListLoader.loadElement();\" /><br/>\n");
        w.append("<table id=\"tblNm\" border=\"1\">\n</table>\n");
        w.append("<input type=\"button\" onclick=\"goToManageClub();\" value=\"Manage Club\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddClub();\" value=\"Add Club\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToEntityInstancePortal();\" value=\"Manage instances\" /><br/>\n");
    }
}
