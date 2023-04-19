package com.darts.servlet.html;

import com.sportservlet.html.ManageH2HMatch;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageDartsMatch extends ManageH2HMatch {
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "MatchOverview?" + compSeasonUrlParameters;
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
