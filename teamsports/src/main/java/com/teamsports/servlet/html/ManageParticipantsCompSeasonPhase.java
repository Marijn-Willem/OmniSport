package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageParticipantsCompSeasonPhase extends com.sportservlet.html.ManageParticipantsCompSeasonPhase {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "KnockoutMain?" + compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
