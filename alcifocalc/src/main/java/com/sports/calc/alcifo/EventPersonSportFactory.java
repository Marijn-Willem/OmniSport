package com.sports.calc.alcifo;

import com.sports.entity.EventPartPersonSport;
import com.sports.entity.EventPersonSport;
import com.sports.entity.PersonSport;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonPersonSportManager;
import com.sports.entity.manager.EventPersonSportManager;
import com.sports.logic.factory.ParticipantType;

import java.sql.Statement;

public class EventPersonSportFactory extends AlcifoParticipantFactory<CompSeasonPersonSportKey,
        SuperKeyEntity,
        PersonSport,
        EventPersonSportKey,
        EventPersonSport,
        EventPartPersonSportKey,
        EventPartPersonSport> {
    @Override
    public EventPersonSportManager getManager(Statement stat) {
        return new EventPersonSportManager(stat);
    }

    @Override
    public CompSeasonPersonSportManager getCompSeasonParticipantManager(Statement stat) {
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
    public AlcifoPartParticipantFactory<CompSeasonPersonSportKey,
            SuperKeyEntity,
            PersonSport,
            EventPersonSportKey,
            EventPersonSport,
            EventPartPersonSportKey,
            CompSeasonEventPartKey,
            EventPartPersonSport,
            EventPartPersonSportKey,
            EventPartPersonSport> getEventPartParticipantFactory() {
        return new EventPartPersonSportFactory();
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.PERSON_SPORT;
    }
}
