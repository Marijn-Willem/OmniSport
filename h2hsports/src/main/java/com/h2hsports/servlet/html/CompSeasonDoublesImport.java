package com.h2hsports.servlet.html;

import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.html.SuperEntityImport;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonDoublesImport extends SuperEntityImport {
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    protected String getOnClick(HttpServletRequest req) {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));

        return "processImport(" + competitionId + ", " + seasonId + ");";
    }

    public void initSpecificProperties(HttpServletRequest req) {

    }

    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("compseasondoubles");
        cssList.add("styling");
    }
}
