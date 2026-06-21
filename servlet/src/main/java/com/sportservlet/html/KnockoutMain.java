package com.sportservlet.html;

import com.sports.entity.CompSeasonPhase;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonPhaseManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class KnockoutMain extends SuperHtmlServlet implements AbstractHtmlServlet {
    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("knockout");
        initSpecificProperties(req);
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);

        List<CompSeasonPhase> compSeasonPhases = cspm.getKnockoutCompSeasonPhases(compSeasonKey);
        new com.sports.calc.h2hsports.DbCalculation(stat).setPhaseDescriptionsFromTypes(compSeasonPhases);

        if (!compSeasonPhases.isEmpty()) {
            Writer w = res.getWriter();

            w.append("<div>\n<select id=\"pid\">\n");

            for (CompSeasonPhase compSeasonPhase : compSeasonPhases) {
                int pid = compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId();

                String line = "<option value=\"" + pid + "\"";

                if (("" + pid).equals(req.getParameter("pid")))
                    line += " selected";

                line += ">" + compSeasonPhase.getDescription() + "</option>\n";

                w.append(line);
            }

            w.append("</select>\n</div>\n");

            w.append("<input type=\"button\" onclick=\"createKnockoutMatchesLoader.loadElement();\" " +
                    "value=\"Create knockout matches\" />\n");
            w.append("<br/><input type=\"button\" onclick=\"goToManagePhaseParticipants();\" " +
                    "value=\"Manage Participants CompSeasonPhase\" />\n");

            w.append("<div id=\"div_res\"></div>\n");
        }
    }
}
