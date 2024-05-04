package com.sports.calc.alcifo;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.EventPartTeam;
import com.sports.entity.EventTeam;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.key.EventPartTeamKey;
import com.sports.entity.key.EventTeamKey;
import com.sports.entity.manager.EventPartTeamManager;
import com.sports.entity.manager.TeamManager;

import java.sql.Statement;

public class EventPartTeamFactory extends AlcifoPartParticipantFactory<CompSeasonTeamKey,
        CompSeasonTeam,
        Team,
        EventTeamKey,
        EventTeam,
        EventPartTeamKey,
        CompSeasonEventPartKey,
        EventPartTeam,
        EventPartTeamKey,
        EventPartTeam> {
    @Override
    public EventPartTeamManager getManager(Statement stat) {
        return new EventPartTeamManager(stat);
    }

    @Override
    public TeamManager getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    @Override
    public EventPartTeamKey getKey(CompSeasonEventPartKey partKey, int participantId) {
        return new EventPartTeamKey(partKey, participantId);
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(CompSeasonEventPartKey partKey) {
        return partKey;
    }

    @Override
    public EventPartTeam getInstance() {
        return new EventPartTeam();
    }

    @Override
    public AlcifoParticipantFactory<CompSeasonTeamKey,
            CompSeasonTeam,
            Team,
            EventTeamKey,
            EventTeam,
            EventPartTeamKey,
            EventPartTeam> getParticipantFactory() {
        return new EventTeamFactory();
    }
}
