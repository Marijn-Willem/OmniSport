package com.sportservlet.html;

import com.sports.calc.alcifo.EventPartPersonSportFactory;
import com.sports.entity.PersonSport;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonTeamPersonSportKey;
import com.sports.entity.manager.CompSeasonTeamPersonSportManager;
import com.sports.entity.manager.TeamManager;
import com.sports.logic.util.Util;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ManageEventPartPersonSports extends ManageAlcifoPartParticipants<PersonSport, CompSeasonEventPartKey> {
    private Map<Integer, Team> personSportTeamMap;

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        List<CompSeasonTeamPersonSportKey> teamPersonSportKeys = new CompSeasonTeamPersonSportManager(stat)
                .getKeysForCompSeason(compSeasonKey);
        List<Integer> teamIds = teamPersonSportKeys.stream().map(key ->
                key.getSuperKey().getSpecificId()).toList();
        Map<Integer, Team> teamMap = new TeamManager(stat).getParticipantMap(teamIds);

        personSportTeamMap = new HashMap<>();

        teamPersonSportKeys.forEach(teamPersonSportKey ->
                personSportTeamMap.put(teamPersonSportKey.getPersonSportId(),
                        teamMap.get(teamPersonSportKey.getSuperKey().getSpecificId())));
    }

    @Override
    String getNameString(PersonSport participant) {
        int pId = participant.getId();
        String teamDescription = personSportTeamMap.containsKey(pId) ? personSportTeamMap.get(pId).getDescription() : null;

        return Util.concatStringsWithDelimiter(super.getNameString(participant),
                Util.getStringBetweenBracketsOrEmptyString(teamDescription), " - ");
    }

    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    EventPartPersonSportFactory getFactory() {
        return new EventPartPersonSportFactory();
    }

    @Override
    void initSpecificJsProperties() {
        jsList.add("eventpartpersonsport");
    }
}
