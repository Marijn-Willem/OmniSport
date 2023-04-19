package com.teamsports.servlet.html;

import com.sports.entity.ActionType;
import com.sports.entity.Team;
import com.sports.entity.TeamMatch;
import com.sports.entity.TeamMatchPart;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.comparator.TeamMatchPartName;
import com.sports.entity.key.TeamMatchKey;
import com.sports.entity.manager.TeamManager;
import com.sports.entity.manager.TeamMatchManager;
import com.sports.entity.manager.TeamMatchPartManager;
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

public class ManageTeamMatchAction extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "MatchTimeLine?" + compSeasonUrlParameters + "&mid=" + req.getParameter("mid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("teammatchaction");
        cssList.add("styling");
    }

    @Override
    protected String getEntityIdName() {
        return null;
    }

    private final LinkedHashMap<Integer, String> actionTypeMap = new LinkedHashMap<>() {{
        ActionType.nameIdMap.forEach((k, v) -> put(v, k));
    }};

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("mid", getIntValuedParameterValue(req, "mid"), w);
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        TeamMatchKey teamMatchKey = new TeamMatchKey(compSeasonKey, getIntValuedParameterValue(req, "mid"));

        TeamMatchManager tmm = new TeamMatchManager(stat);
        TeamManager tm = new TeamManager(stat);
        TeamMatchPartManager tmpm = new TeamMatchPartManager(stat);

        TeamMatch teamMatch = tmm.getEntityFromSuperKey(teamMatchKey);
        List<Integer> teamIds = new ArrayList<>() {{
            if (teamMatch.getTeamHomeId() != null)
                add(teamMatch.getTeamHomeId());

            if (teamMatch.getTeamAwayId() != null)
                add(teamMatch.getTeamAwayId());
        }};

        List<Team> teams = tm.getTeamList(teamIds);
        teams.sort(new DescribedEntityDescription());
        LinkedHashMap<Integer, String> teamMap = new LinkedHashMap<>() {{
            teams.forEach(x -> put(x.getId(), x.getDescription()));
        }};

        List<TeamMatchPart> teamMatchParts = tmpm.getTeamMatchParts(teamMatchKey);
        teamMatchParts.sort(new TeamMatchPartName());
        LinkedHashMap<Integer, String> matchPartMap = new LinkedHashMap<>() {{
            put(0, "-");
            teamMatchParts.forEach(x -> put(x.getTeamMatchPartId(), x.getName()));
        }};

        Writer w = res.getWriter();

        writeSelectWithLabel("Action Type", "at", actionTypeMap, null, w);
        writeSelectWithLabel("Team", "tid", teamMap, null, false, w);
        writeNumericTextField("Minute", "m", null, w);
        writeSelectWithLabel("Match part", "mpid", matchPartMap, null, w);
    }

    @Override
    protected String getUpdateId(HttpServletRequest req) {
        return "";
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }
}
