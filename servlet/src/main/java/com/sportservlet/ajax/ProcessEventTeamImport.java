package com.sportservlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.CompSeasonTeam;
import com.sports.entity.EventTeam;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.key.EventTeamKey;
import com.sports.entity.key.TeamDescriptionSportIdGenderIdKey;
import com.sports.entity.manager.*;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessEventTeamImport extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String message;

        String[] teamNames = (String[]) req.getSession().getAttribute("teamNames");
        req.getSession().removeAttribute("teamNames");

        if (teamNames != null) {
            int spid = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
            int eid = getIntValuedParameterValue(req, "eid");

            CompSeasonEventKey cseKey = new CompSeasonEventKey(compSeasonKey, eid);
            EventTeamManager etm = new EventTeamManager(stat);

            if (etm.getParticipantIdsInEvent(cseKey).size() == 0) {
                CompSeasonTeamManager cstm = new CompSeasonTeamManager(stat);
                Set<Integer> existingIds = new HashSet<>(cstm.getParticipantIdsCompSeason(compSeasonKey));
                List<Integer> idsToInsertInCompSeason = new ArrayList<>();

                CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);
                int gid = new SportEventManager(stat).getEntityFromSuperKey(cse.getSportEventKey()).getGenderId();

                List<TeamDescriptionSportIdGenderIdKey> teamKeys = Arrays.stream(teamNames)
                        .map(x -> new TeamDescriptionSportIdGenderIdKey(x, spid, gid)).collect(Collectors.toList());

                List<Integer> teamIds = new ArrayList<>(new TeamManager(stat).getTeamMapFromDescrSportGender(teamKeys).values());

                teamIds.forEach(tid -> {
                    if (!existingIds.contains(tid))
                        idsToInsertInCompSeason.add(tid);
                });

                Map<CompSeasonTeamKey, CompSeasonTeam> compSeasonTeamMap = new HashMap<>() {{
                    idsToInsertInCompSeason.forEach(id -> put(new CompSeasonTeamKey(compSeasonKey, id), new CompSeasonTeam()));
                }};

                Map<EventTeamKey, EventTeam> eventTeamMap = new HashMap<>() {{
                    teamIds.forEach(id -> put(new EventTeamKey(cseKey, id), new EventTeam()));
                }};

                cstm.insertCompSeasonTeamMap(compSeasonTeamMap);
                etm.insertParticipantMap(eventTeamMap);

                message = "Teams successfully inserted";
            }
            else
                message = "Already teams in this event";
        }
        else
            message = "No teams selected";

        resp.getWriter().append(message);
    }
}
