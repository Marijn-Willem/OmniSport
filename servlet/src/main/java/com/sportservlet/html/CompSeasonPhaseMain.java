package com.sportservlet.html;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class CompSeasonPhaseMain extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected abstract String getRefreshUrl();
    protected abstract String getManageCompSeasonPhaseUrl();

    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("compseasonphase");
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));

        CompSeasonKey csk = new CompSeasonKey(competitionId, seasonId);

        List<CompSeasonPhase> compSeasonPhaseList = new CompSeasonPhaseManager(stat).getCompSeasonPhases(csk);
        DbCalculation dbCalculation = new DbCalculation(stat);
        dbCalculation.setPhaseDescriptionsFromTypes(compSeasonPhaseList);
        dbCalculation.setCanBeDeleted(compSeasonPhaseList);

        compSeasonPhaseList.sort(new CompSeasonPhaseRoundDescription());

        Writer w = res.getWriter();

        w.append("<table border=\"1\">\n");

        for (CompSeasonPhase compSeasonPhase : compSeasonPhaseList)
            w.append(getTableRow(compSeasonPhase));

        w.append("</table>\n");

        w.append("<span>");
        w.append(getLink("i", competitionId, seasonId, null, "Add new Phase"));
        w.append("</span>\n");
    }

    private String getTableRow(CompSeasonPhase compSeasonPhase) {
        int compId = compSeasonPhase.getCompSeasonPhaseKey().getCompetitionId();
        int seasonId = compSeasonPhase.getCompSeasonPhaseKey().getSeasonId();
        int phaseId = compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId();

        return "<tr>" + (compSeasonPhase.isCanBeDeleted() ? "<td onclick=\"deletePhase('" + getRefreshUrl() +
                "', " + phaseId + ");\">X</td>" : "<td></td>") + "<td>" +
                getLink("u", compId, seasonId, phaseId, compSeasonPhase.getDescription()) + "</td></tr>\n";
    }

    private String getLink(String mode, int competitionId, int seasonId, Integer phaseId, String text) {
        return "<a href=\"" + path + "/" + getManageCompSeasonPhaseUrl() + "?md=" + mode + "&cid=" + competitionId +
                "&sid=" + seasonId + (phaseId != null ? "&pid=" + phaseId : "") + "\">" + text + "</a><br/>";
    }
}
