package com.teamsports.servlet.html;

import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.CompDivision;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MatchMatrixDivision extends SuperHtmlServlet {
    @Override
    void initSpecificProperties(HttpServletRequest req) {
        jsList.add("standing");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initMatchMatrixDivision();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonPhaseVarsInScriptTag(req, w);
        w.append("const oc = false;\n");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "StandingByCompSeason?" + compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        List<CompDivision> compDivisions = new DbCalculation(stat).getSortedCompDivisions(compSeasonKey);

        Writer w = res.getWriter();

        for (CompDivision compDivision : compDivisions) {
            if (compDivision.getParentDivisionId() != null) {
                w.append("<span><input type=\"checkbox\" value=\"");
                w.append(Integer.toString(compDivision.getCompDivisionId()));
                w.append("\" checked onclick=\"handleClickDivision(this);\" />");
                w.append(compDivision.getName());
                w.append("</span><br/>\n");
            }
        }

        w.append("<table id=\"tblMatchMatrix\" border=\"1\">\n</table>\n");
    }
}
