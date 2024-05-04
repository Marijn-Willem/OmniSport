package com.sports.calc.alcifo;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.EventPartTeam;
import com.sports.entity.EventTeam;
import com.sports.entity.Team;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.EventTeamManager;
import com.sports.logic.factory.ParticipantType;

import java.sql.Statement;

public class EventTeamFactory extends AlcifoParticipantFactory<CompSeasonTeamKey,
        CompSeasonTeam,
        Team,
        EventTeamKey,
        EventTeam,
        EventPartTeamKey,
        EventPartTeam> {
    @Override
    public EventTeamManager getManager(Statement stat) {
        return new EventTeamManager(stat);
    }

    @Override
    public CompSeasonTeamManager getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonTeamManager(stat);
    }

    @Override
    public EventTeamKey getAlcifoParticipantKey(CompSeasonEventKey cseKey, int participantId) {
        return new EventTeamKey(cseKey, participantId);
    }

    @Override
    public EventTeam getAlcifoParticipant() {
        return new EventTeam();
    }

    @Override
    public AlcifoPartParticipantFactory<CompSeasonTeamKey,
            CompSeasonTeam,
            Team,
            EventTeamKey,
            EventTeam,
            EventPartTeamKey,
            CompSeasonEventPartKey,
            EventPartTeam,
            EventPartTeamKey,
            EventPartTeam> getEventPartParticipantFactory() {
        return new EventPartTeamFactory();
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.TEAM;
    }
}
