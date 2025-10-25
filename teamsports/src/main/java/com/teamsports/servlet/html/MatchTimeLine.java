package com.teamsports.servlet.html;

import com.sports.entity.ActionType;
import com.sports.entity.Team;
import com.sports.entity.TeamMatch;
import com.sports.entity.TeamMatchAction;
import com.sports.entity.comparator.MatchActionMinute;
import com.sports.entity.key.TeamMatchKey;
import com.sports.entity.manager.TeamManager;
import com.sports.entity.manager.TeamMatchActionManager;
import com.sports.entity.manager.TeamMatchManager;
import com.sports.logic.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MatchTimeLine extends SuperHtmlServlet {
    private int matchId;
    private TeamMatchKey teamMatchKey;
    private TeamMatch teamMatch;

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        matchId = getIntValuedParameterValue(req, "mid");
        teamMatchKey = new TeamMatchKey(compSeasonKey, matchId);
        teamMatch = new TeamMatchManager(stat).getMatches(Collections.singletonList(teamMatchKey)).get(0);
    }

    void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        String basicParameterString = compSeasonUrlParameters + "&pid=" + getIntValuedParameterValue(req, "pid");

        return teamMatch.getParentMatchId() != null ?
                "ParentMatchPortal?" + basicParameterString + "&pmid=" + teamMatch.getParentMatchId() :
                "MatchOverview?" + basicParameterString;
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Integer teamHomeId = teamMatch.getTeamHomeId();
        Integer teamAwayId = teamMatch.getTeamAwayId();

        List<Integer> teamIds = new ArrayList<>() {{
            if (teamHomeId != null)
                add(teamHomeId);

            if (teamAwayId != null)
                add(teamAwayId);
        }};

        Map<Integer, Team> teamMap = new TeamManager(stat).getTeamMap(teamIds);
        List<TeamMatchAction> matchActions = new TeamMatchActionManager(stat).getMatchActionsMatch(teamMatchKey);

        matchActions.sort(new MatchActionMinute());

        Writer w = res.getWriter();

        String rule = "<table border=\"1\">\n<tr><th>" + getTeamDescription(teamHomeId, teamMap) +
                "</th><th>Minute</th><th>" + getTeamDescription(teamAwayId, teamMap) + "</th></tr>\n";

        w.append(rule);

        for (TeamMatchAction matchAction : matchActions) {
            String text = ActionType.getNameFromId(matchAction.getActionTypeId());

            if (teamMatch.getTeamHomeId() == matchAction.getTeamId())
                rule = "<tr><td>" + text + "</td><td>" +
                        Util.convertIntegerToString(matchAction.getMinute()) + "</td><td/><tr/>\n";
            else
                rule = "<tr><td/><td>" + Util.convertIntegerToString(matchAction.getMinute()) +
                        "</td><td>" + text + "</td></tr>\n";

            w.append(rule);
        }

        int pid = getIntValuedParameterValue(req, "pid");

        w.append("</table>\n");
        writeLink("ManageH2HMatch?" + compSeasonUrlParameters + "&pid=" + pid + "&mid=" + matchId + "&from=tl",
                "Manage Match", w);
        writeLink("ManageTeamMatchAction?" + compSeasonUrlParameters+ "&pid=" + pid + "&mid=" + matchId + "&md=i",
                "Insert match action", w);
    }

    private String getTeamDescription(Integer teamId, Map<Integer, Team> teamMap) {
        Team team = teamMap.get(teamId);
        String description = team != null ? team.getDescription() : null;

        return Util.convertEmptyString(description, "-");
    }
}
