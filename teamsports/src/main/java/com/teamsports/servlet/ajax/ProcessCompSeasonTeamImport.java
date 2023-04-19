package com.teamsports.servlet.ajax;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.Competition;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.TeamManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProcessCompSeasonTeamImport extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String[] names = (String[])req.getSession().getAttribute("teamNames");

        if (names != null) {
            CompSeasonTeamManager cstm = new CompSeasonTeamManager(stat);

            List<CompSeasonTeam> compSeasonTeams = cstm.getTeamsInCompSeason(compSeasonKey);

            String response;

            if (compSeasonTeams.size() == 0) {
                Competition competition = new CompetitionManager(stat).getCompetition(competitionId);
                Map<String, Team> teamMap = new TeamManager(stat).getDescrTeamMapBySportGender(Arrays.asList(names),
                        competition.getSportId(), competition.getGenderId());
                List<CompSeasonTeamKey> keys = new ArrayList<>();

                for (Team team : teamMap.values())
                    keys.add(new CompSeasonTeamKey(compSeasonKey, team.getId()));

                cstm.insertCompSeasonParticipants(keys);

                response = "Teams successfully imported";
            }
            else
                response = "Already teams in this competition season";

            req.getSession().removeAttribute("teamNames");
            resp.getWriter().append(response);
        }
    }
}
