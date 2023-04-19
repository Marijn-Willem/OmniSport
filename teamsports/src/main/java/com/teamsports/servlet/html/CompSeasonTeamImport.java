package com.teamsports.servlet.html;

import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.html.SuperEntityImport;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonTeamImport extends SuperEntityImport {
    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    protected String getOnClick(HttpServletRequest req) {
        return "processCompSeasonTeamImport();";
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsSpecificList.add("compseasonteam");
        cssList.add("styling");
    }
}
