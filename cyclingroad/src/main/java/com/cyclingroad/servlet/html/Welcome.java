package com.cyclingroad.servlet.html;

import com.sports.entity.Sport;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class Welcome extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<h1>Welcome</h1>");

        ServletUtil.writeGenericGoToButton("CompSeasonPortal", "spid=" + Sport.sportIdCyclingRoad, "Competition Seasons", w);
        ServletUtil.writeGenericGoToButton("PersonSearch", "", "Manage Persons", w);
        ServletUtil.writeGenericGoToButton("ManageTeams", "", "Manage Teams", w);
    }
}
