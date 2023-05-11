package com.sports.calc.alcifo;

import com.sports.entity.EventPartTeam;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartTeamKey;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.entity.manager.EventPartTeamManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.entity.manager.TeamManager;

import java.sql.Statement;

public class EventPartTeamFactory implements AlcifoPartParticipantFactory {
    @Override
    public AlcifoPartParticipantManager<EventPartTeamKey, CompSeasonEventPartKey, EventPartTeam> getManager(Statement stat) {
        return new EventPartTeamManager(stat);
    }

    @Override
    public ParticipantManager<Team> getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    @Override
    public EventPartTeamKey getKey(SuperKey partKey, int participantId) {
        return new EventPartTeamKey((CompSeasonEventPartKey)partKey, participantId);
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(SuperKey partKey) {
        return (CompSeasonEventPartKey)partKey;
    }

    @Override
    public EventPartTeam getInstance() {
        return new EventPartTeam();
    }

    @Override
    public AlcifoParticipantFactory getParticipantFactory() {
        return new EventTeamFactory();
    }
}
