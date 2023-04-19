package com.management.servlet.html;

import com.sportservlet.html.TeamSelectServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class TeamPortal extends TeamSelectServlet {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("team");
        cssList.add("styling");
    }

    @Override
    protected void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<input type=\"button\" onclick=\"goToManageTeam();\" value=\"Manage Team\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddTeam();\" value=\"Add Team\" /><br/>\n");
    }
}
