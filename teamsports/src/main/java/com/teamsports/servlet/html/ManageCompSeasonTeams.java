package com.teamsports.servlet.html;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.Competition;
import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.ClubManager;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.TeamManager;
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

public class ManageCompSeasonTeams extends SuperHtmlServlet {
    void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonteam");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonTeamPortal?" + compSeasonUrlParameters;
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        CompSeasonTeamManager cstm = new CompSeasonTeamManager(stat);
        List<CompSeasonTeam> compSeasonTeams = cstm.getTeamsInCompSeason(compSeasonKey);

        if (!compSeasonTeams.isEmpty())
            dispatchToReturnPath(req, res);
        else {
            Competition competition = new CompetitionManager(stat).getCompetition(competitionId);
            // Competition assumed to be domestic
            List<Integer> clubIds = new ClubManager(stat).getClubIdsFromCountryGeo(competition.getGeoId());

            List<Team> relevantTeams = new TeamManager(stat).getTeamListForSportGenderClubs(competition.getSportId(),
                    competition.getGenderId(), clubIds);

            List<Integer> teamIdsPreviousCompSeason = new ArrayList<>();

            CompSeasonKey prevCompSeasonKey = new DbCalculation(stat).getPreviousCompSeason(compSeasonKey);

            if (prevCompSeasonKey != null) {
                teamIdsPreviousCompSeason = new CompSeasonTeamManager(stat).getParticipantIdsCompSeason(prevCompSeasonKey);
                Collections.sort(teamIdsPreviousCompSeason);
            }

            relevantTeams.sort(new DescribedEntityDescription());

            Writer w = res.getWriter();

            for (Team team : relevantTeams) {
                w.append("<input class=\"cb\" type=\"checkbox\" name=\"tid\" value=\"");
                w.append(Integer.toString(team.getId()));
                w.append("\"");
                if (Collections.binarySearch(teamIdsPreviousCompSeason, team.getId()) >= 0)
                    w.append(" checked");
                w.append(" />");
                w.append(team.getDescription());
                w.append("<br/>\n");
            }

            w.append("<br/>\n");
            w.append("<input id=\"btnSnd\" type=\"button\" onclick=\"handleSend();\" value=\"Send\" /><br/>\n");
            w.append("<div id=\"divUpd\"></div>\n");
        }
    }
}
