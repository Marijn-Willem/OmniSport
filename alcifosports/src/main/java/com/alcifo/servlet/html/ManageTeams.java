package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ManageTeams extends com.sportservlet.html.ManageTeams {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "SportOverview";
    }
}
