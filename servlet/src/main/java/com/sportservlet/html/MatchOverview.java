package com.sportservlet.html;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class MatchOverview extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected abstract String getAddMatchLink();

    protected String getParametersAddMatchLink() { return null; }

    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("matchoverview");
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeInitStateVarInScriptTag("pid", req, w);
        if (getAddMatchLink() != null)
            ServletUtil.writeStringConst("aml", getAddMatchLink(), w);

        if (getParametersAddMatchLink() != null)
            ServletUtil.writeStringConst("amlP", getParametersAddMatchLink(), w);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"init();\">\n");
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        List<CompSeasonPhase> compSeasonPhases = new CompSeasonPhaseManager(stat).getNonKnockoutCompSeasonPhases(compSeasonKey);
        new DbCalculation(stat).setPhaseDescriptionsFromTypes(compSeasonPhases);
        compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());

        int initSelIndX = 0;

        for (int i = 0; i < compSeasonPhases.size(); i++)
            if (!compSeasonPhases.get(i).isFinished()) {
                initSelIndX = i;
                break;
            }

        Writer w = res.getWriter();

        w.append("<div>\n<select id=\"selPid\" onchange=\"loadMatchesCompSeasonPhase();\">\n");

        String line;

        for (int i = 0; i < compSeasonPhases.size(); i++) {
            CompSeasonPhase compSeasonPhase = compSeasonPhases.get(i);

            line = "<option value=\"" + compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId() + "\"" +
                    (i == initSelIndX ? " selected=\"selected\"" : "") +
                    (compSeasonPhase.isHasStanding() ? " class=\"standing\"" : "") + ">" +
                    compSeasonPhase.getDescription() + "</option>\n";

            w.append(line);
        }

        w.append("</select>\n</div>\n");
        w.append("<table id=\"tbl_matches\" class=\"overview\"></table>\n");
        w.append("<table id=\"tbl_standing\" class=\"overview\"></table>\n");
        w.append("<table id=\"tbl_matchmatrix\" class=\"overview\" border=\"1\"></table>\n");

        if (getAddMatchLink() != null) {
            String rule = "<div>\n<input type=\"button\" onclick=\"goToAddMatch();\" value=\"Add match\" />\n</div>\n";
            w.append(rule);
        }
    }
}
