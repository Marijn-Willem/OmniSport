package com.teamsports.servlet.html;

import com.sports.entity.CompDivision;
import com.sports.entity.CompSeasonTeam;
import com.sports.entity.Team;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.key.CompSeasonDivisionKey;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.manager.CompDivisionManager;
import com.sports.entity.manager.CompSeasonDivisionManager;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.TeamManager;
import com.sports.logic.util.Util;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ManageCompSeasonTeam extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonTeamPortal?cid=" + competitionId + "&sid=" + seasonId;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonteam");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "tid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("tid", getIntValuedParameterValue(req, "tid"), w);
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int teamId = getIntValuedParameterValue(req, "tid");
        Team team = new TeamManager(stat).getEntityFromId(teamId);

        CompSeasonTeamKey cstk = new CompSeasonTeamKey(compSeasonKey, teamId);
        CompSeasonTeam cst = new CompSeasonTeamManager(stat).getCompSeasonTeam(cstk);

        List<CompSeasonDivisionKey> csdKeys = new CompSeasonDivisionManager(stat).getCompSeasonDivisions(compSeasonKey);
        List<CompDivisionKey> compDivisionKeys = new ArrayList<>();

        for (CompSeasonDivisionKey csdKey : csdKeys)
            compDivisionKeys.add(new CompDivisionKey(competitionId, csdKey.getCompDivisionId()));

        List<CompDivision> compDivisions = new CompDivisionManager(stat).getCompDivisionList(compDivisionKeys);
        compDivisions.sort(new NamedEntityName());
        LinkedHashMap<Integer, String> compDivisionMap = new LinkedHashMap<>();
        compDivisionMap.put(0, "-");

        for (CompDivision compDivision : compDivisions)
            compDivisionMap.put(compDivision.getCompDivisionId(), compDivision.getName());

        Writer w = res.getWriter();

        w.append("<h2>");
        w.append(Util.convertNullStringToEmpty(team.getDescription()));
        w.append("</h2>\n");
        writeSelectWithLabel("Comp Division", "did", compDivisionMap, cst.getCompDivisionId(), w);
    }

    @Override
    protected String getUpdateId(HttpServletRequest req) {
        return req.getParameter("tid");
    }
}
