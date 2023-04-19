package com.sports.calc.alcifo;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.EventTeam;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.key.EventTeamKey;
import com.sports.entity.manager.AlcifoParticipantManager;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.EventTeamManager;

import java.sql.Statement;

public class EventTeamFactory implements AlcifoParticipantFactory {
    @Override
    public AlcifoParticipantManager<EventTeamKey, EventTeam> getManager(Statement stat) {
        return new EventTeamManager(stat);
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonTeamKey, CompSeasonTeam> getCompSeasonParticipantManager(Statement stat) {
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
    public AlcifoPartParticipantFactory getEventPartParticipantFactory() {
        return new EventPartTeamFactory();
    }

    @Override
    public AlcifoPartParticipantFactory getDisciplinePartParticipantFactory() {
        return new DisciplinePartTeamFactory();
    }
}
