package com.sports.calc.alcifo;

import com.sports.entity.EventPartPersonSport;
import com.sports.entity.EventPersonSport;
import com.sports.entity.PersonSport;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.entity.manager.PersonSportManager;

import java.sql.Statement;

public class EventPartPersonSportFactory extends AlcifoPartParticipantFactory<CompSeasonPersonSportKey,
        SuperKeyEntity,
        PersonSport,
        EventPersonSportKey,
        EventPersonSport,
        EventPartPersonSportKey,
        CompSeasonEventPartKey,
        EventPartPersonSport,
        EventPartPersonSportKey,
        EventPartPersonSport> {
    @Override
    public EventPartPersonSportManager getManager(Statement stat) {
        return new EventPartPersonSportManager(stat);
    }

    @Override
    public ParticipantManager<PersonSport> getParticipantManager(Statement stat) {
        return new PersonSportManager(stat);
    }

    @Override
    public EventPartPersonSportKey getKey(CompSeasonEventPartKey partKey, int participantId) {
        return new EventPartPersonSportKey(partKey, participantId);
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(CompSeasonEventPartKey partKey) {
        return partKey;
    }

    @Override
    public EventPartPersonSport getInstance() {
        return new EventPartPersonSport();
    }

    @Override
    public AlcifoParticipantFactory<CompSeasonPersonSportKey,
            SuperKeyEntity,
            PersonSport,
            EventPersonSportKey,
            EventPersonSport,
            EventPartPersonSportKey,
            EventPartPersonSport> getParticipantFactory() {
        return new EventPersonSportFactory();
    }
}
