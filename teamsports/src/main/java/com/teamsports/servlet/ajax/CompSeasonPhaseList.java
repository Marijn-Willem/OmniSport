package com.teamsports.servlet.ajax;

import com.sports.entity.CompDivision;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonPhaseList extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        boolean showDivisions = Boolean.parseBoolean(Util.convertEmptyString(req.getParameter("sd"), "true"));
        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);
        List<CompSeasonPhase> compSeasonPhases = cspm.getCompSeasonPhases(compSeasonKey);
        List<CompDivision> compDivisions = new com.sports.calc.teamsports.DbCalculation(stat).getSortedCompDivisions(compSeasonKey);

        new com.sports.calc.h2hsports.DbCalculation(stat).setPhaseDescriptionsFromTypes(compSeasonPhases);
        compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());

        Writer w = resp.getWriter();

        for (CompSeasonPhase compSeasonPhase : compSeasonPhases)
            if (showDivisions)
                writeOptions(compSeasonPhase, compDivisions, w);
            else
                writeOptions(compSeasonPhase, w);
    }

    private void writeOptions(CompSeasonPhase csp, List<CompDivision> divisionList, Writer w) throws IOException {
        if (csp.isHasDivisionStandings())
            for (CompDivision division : divisionList) {
                String value = Util.concatStringsWithDelimiter(
                        "" + csp.getCompSeasonPhaseKey().getCompSeasonPhaseId(),
                        "" + division.getCompDivisionId(), "_");

                String name = Util.concatStringsWithDelimiter(csp.getDescription(), division.getName(), " - ");

                writeOption("stand", value, name, w);
            }
        else
            writeOptions(csp, w);
    }

    private void writeOptions(CompSeasonPhase csp, Writer w)
            throws IOException {
        writeOption(getCssClass(csp), "" + csp.getCompSeasonPhaseKey().getCompSeasonPhaseId(),
                csp.getDescription(), w);
    }

    private void writeOption(String cssClass, String value, String name, Writer w) throws IOException {
        w.append("<option class=\"");
        w.append(cssClass);
        w.append("\" value=\"");
        w.append(value);
        w.append("\">");
        w.append(name);
        w.append("</option>\n");
    }

    private String getCssClass(CompSeasonPhase csp) {
        String cssClass = "";

        if (csp.isHasStanding())
            cssClass += "stand";
        else
            cssClass += "noStand";

        if (csp.isKnockoutParent())
            cssClass += " ko";

        return cssClass;
    }
}
