package com.teamsports.servlet.ajax;

import com.sports.entity.Team;
import com.sports.entity.TeamMatch;
import com.sports.entity.comparator.MatchDate;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseTeamManager;
import com.sports.entity.manager.TeamManager;
import com.sports.entity.manager.TeamMatchManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.teamsports.servlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class MatchList extends SuperResponseServlet {
    protected abstract List<TeamMatch> getMatchList(TeamMatchManager mm, CompSeasonPhaseKey cspk) throws SQLException;

    protected void processTeamMap(Statement stat, Map<Integer, Team> teamMap) throws SQLException {

    }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int phaseId = Integer.parseInt(req.getParameter("pid"));

        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(compSeasonKey, phaseId);

        CompSeasonPhaseTeamManager csptm = new CompSeasonPhaseTeamManager(stat);
        TeamManager tm = new TeamManager(stat);
        TeamMatchManager mm = new TeamMatchManager(stat);

        List<Integer> teamIds = csptm.getTeamsInCompSeasonPhases(Collections.singletonList(cspk));
        Map<Integer, Team> teamMap = tm.getTeamMap(teamIds);
        processTeamMap(stat, teamMap);
        List<TeamMatch> teamMatchList = getMatchList(mm, cspk);

        if (teamMap != null && teamMatchList != null) {
            teamMatchList.sort(new MatchDate());

            writeMatchTable(teamMatchList, cspk, teamMap, resp.getWriter());
        }
    }

    private void writeMatchTable(List<TeamMatch> matchesSorted, CompSeasonPhaseKey compSeasonPhaseKey,
                                 Map<Integer, Team> teamMap, Writer w) throws IOException {
        String rule = "<tr><th>Home</th><th>Away</th><th colspan=\"2\">Score</th><th>Date</th></tr>\n";
        w.append(rule);

        for (TeamMatch teamMatch : matchesSorted) {
            String onClick = " onclick=\"goToMatchTimeLine(" + compSeasonPhaseKey.getSepValues(", ") +
                    ", " + teamMatch.getTeamMatchId() + ")\"";

            rule = "<tr" + onClick + ">" +
                    getTdInMatchTable(teamMatch.getTeamHomeId(), teamMap, teamMatch.isParticipant1Win()) +
                    getTdInMatchTable(teamMatch.getTeamAwayId(), teamMap, teamMatch.isParticipant2Win()) +
                    "<td>" + teamMatch.getScoreHome() + "</td>" +
                    "<td>" + teamMatch.getScoreAway() + "</td>" +
                    "<td>" + Util.convertEmptyDateTimeToDateString(teamMatch.getDate()) + "</td></tr>\n";

            w.append(rule);
        }
    }

    private String getTdInMatchTable(int id, Map<Integer, Team> teamMap, boolean isWinner) {
        String name = ServletUtil.getTeamName(teamMap.get(id));
        return "<td" + (isWinner ? " class=\"matchWinner\"" : "") + ">" + name + "</td>";
    }
}
