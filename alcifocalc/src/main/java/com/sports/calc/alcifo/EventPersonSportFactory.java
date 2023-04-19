package com.sports.calc.alcifo;

import com.sports.entity.EventPersonSport;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.key.EventPersonSportKey;
import com.sports.entity.manager.AlcifoParticipantManager;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPersonSportManager;
import com.sports.entity.manager.EventPersonSportManager;

import java.sql.Statement;

public class EventPersonSportFactory implements AlcifoParticipantFactory {
    @Override
    public AlcifoParticipantManager<EventPersonSportKey, EventPersonSport> getManager(Statement stat) {
        return new EventPersonSportManager(stat);
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonPersonSportKey, SuperKeyEntity> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonPersonSportManager(stat);
    }

    @Override
    public EventPersonSportKey getAlcifoParticipantKey(CompSeasonEventKey cseKey, int participantId) {
        return new EventPersonSportKey(cseKey, participantId);
    }

    @Override
    public EventPersonSport getAlcifoParticipant() {
        return new EventPersonSport();
    }

    @Override
    public AlcifoPartParticipantFactory getEventPartParticipantFactory() {
        return new EventPartPersonSportFactory();
    }

    @Override
    public AlcifoPartParticipantFactory getDisciplinePartParticipantFactory() {
        return new DisciplinePartPersonSportFactory();
    }
}
