package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ManageTeams extends com.sportservlet.html.ManageTeams {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "SportOverview";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
