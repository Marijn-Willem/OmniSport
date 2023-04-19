package com.teamsports.servlet.ajax;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.CompDivision;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.comparator.CompDivisionParentName;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompDivisionManager;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompSeasonPhaseList extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        boolean showDivisions = Boolean.parseBoolean(Util.convertEmptyString(req.getParameter("sd"), "true"));
        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);
        List<CompSeasonPhase> compSeasonPhases = cspm.getCompSeasonPhases(compSeasonKey);
        List<CompDivision> compDivisions = new CompDivisionManager(stat).getCompDivisions(competitionId);
        Map<CompSeasonPhaseKey, CompSeasonPhase> parentPhaseMap = getParentPhaseMap(cspm, compSeasonPhases);

        new DbCalculation(stat).setPhaseDescriptionsFromTypes(compSeasonPhases);
        compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());
        compDivisions.sort(new CompDivisionParentName());

        Writer w = resp.getWriter();

        for (CompSeasonPhase compSeasonPhase : compSeasonPhases)
            if (showDivisions)
                writeOptions(compSeasonPhase, parentPhaseMap, compDivisions, w);
            else
                writeOptions(compSeasonPhase, parentPhaseMap, w);
    }

    private Map<CompSeasonPhaseKey, CompSeasonPhase> getParentPhaseMap(CompSeasonPhaseManager cspm,
                                                                       List<CompSeasonPhase> compSeasonPhases)
        throws SQLException {
        List<CompSeasonPhaseKey> parentKeys = new ArrayList<>() {{
            compSeasonPhases.forEach(x -> {
                if (x.getParentPhaseKey() != null)
                    add(x.getParentPhaseKey());
            });
        }};

        List<CompSeasonPhase> parentPhases = cspm.getCompSeasonPhases(parentKeys);

        return new HashMap<>() {{
            parentPhases.forEach(x -> put(x.getCompSeasonPhaseKey(), x));
        }};
    }

    private boolean isKnockout(CompSeasonPhase csp, Map<CompSeasonPhaseKey, CompSeasonPhase> parentPhaseMap) {
        return csp.isKnockoutParent() ||
                (csp.getParentPhaseKey() != null && parentPhaseMap.get(csp.getParentPhaseKey()).isKnockoutParent());
    }

    private void writeOptions(CompSeasonPhase csp, Map<CompSeasonPhaseKey, CompSeasonPhase> parentPhaseMap,
                              List<CompDivision> divisionList, Writer w) throws IOException {
        if (csp.isHasDivisionStandings())
            for (CompDivision division : divisionList) {
                String value = Util.concatStringsWithDelimiter(
                        "" + csp.getCompSeasonPhaseKey().getCompSeasonPhaseId(),
                        "" + division.getCompDivisionId(), "_");

                String name = Util.concatStringsWithDelimiter(csp.getDescription(), division.getName(), " - ");

                writeOption("stand", value, name, w);
            }
        else
            writeOptions(csp, parentPhaseMap, w);
    }

    private void writeOptions(CompSeasonPhase csp, Map<CompSeasonPhaseKey, CompSeasonPhase> parentPhaseMap, Writer w)
            throws IOException {
        writeOption(getCssClass(csp, parentPhaseMap), "" + csp.getCompSeasonPhaseKey().getCompSeasonPhaseId(),
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

    private String getCssClass(CompSeasonPhase csp, Map<CompSeasonPhaseKey, CompSeasonPhase> parentPhaseMap) {
        String cssClass = "";

        if (csp.isHasStanding())
            cssClass += "stand";
        else
            cssClass += "noStand";

        if (isKnockout(csp, parentPhaseMap))
            cssClass += " ko";

        return cssClass;
    }
}
