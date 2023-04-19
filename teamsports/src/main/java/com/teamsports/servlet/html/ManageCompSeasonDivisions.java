package com.teamsports.servlet.html;

import com.sports.entity.CompDivision;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.key.CompSeasonDivisionKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompDivisionManager;
import com.sports.entity.manager.CompSeasonDivisionManager;
import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageCompSeasonDivisions extends SuperHtmlServlet {
    private CompSeasonDivisionManager csdm;

    @Override
    void initSpecificProperties(HttpServletRequest req) {
        jsList.add("compseasondivision");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        csdm = new CompSeasonDivisionManager(stat);

        List<CompSeasonDivisionKey> keys = csdm.getCompSeasonDivisions(compSeasonKey);

        if (keys.size() > 0)
            dispatchToReturnPath(req, res);
        else {
            CompDivisionManager cdm = new CompDivisionManager(stat);

            List<CompDivision> compDivisions = cdm.getCompDivisions(competitionId);
            compDivisions.sort(new NamedEntityName());

            List<Integer> previousDivisionIds = getPreviousDivisionIds(stat);

            Writer w = res.getWriter();

            for (CompDivision compDivision : compDivisions) {
                boolean isChecked = Collections.binarySearch(previousDivisionIds, compDivision.getCompDivisionId()) >= 0;
                writeCheckbox(compDivision, isChecked, w);
            }

            w.append("<br/>\n");
            w.append("<input id=\"btnSnd\" type=\"button\" onclick=\"handleSend();\" value=\"Send\" /><br/>\n");
            w.append("<div id=\"divUpd\"></div>\n");
        }
    }

    private List<Integer> getPreviousDivisionIds(Statement stat) throws SQLException {
        List<Integer> previousIds = new ArrayList<>();

        CompSeasonKey previousCompSeason = new DbCalculation(stat).getPreviousCompSeason(compSeasonKey);

        if (previousCompSeason != null) {
            List<CompSeasonDivisionKey> keys = csdm.getCompSeasonDivisions(previousCompSeason);

            for (CompSeasonDivisionKey csdk : keys)
                previousIds.add(csdk.getCompDivisionId());
        }

        Collections.sort(previousIds);

        return previousIds;
    }

    private void writeCheckbox(CompDivision compDivision, boolean isChecked, Writer w) throws IOException {
        w.append("<input class=\"cb\" type=\"checkbox\" name=\"did\" value=\"");
        w.append(Integer.toString(compDivision.getCompDivisionId()));
        w.append("\"");
        if (isChecked)
            w.append(" checked");
        w.append(" />");
        w.append(compDivision.getName());
        w.append("<br/>");
    }
}
