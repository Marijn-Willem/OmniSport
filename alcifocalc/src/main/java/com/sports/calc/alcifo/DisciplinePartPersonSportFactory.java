package com.sports.calc.alcifo;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.DisciplinePartPersonSportManager;
import com.sports.entity.manager.PersonSportManager;

import java.sql.Statement;

public class DisciplinePartPersonSportFactory extends AlcifoPartParticipantFactory<CompSeasonPersonSportKey,
        SuperKeyEntity,
        PersonSport,
        EventPersonSportKey,
        EventPersonSport,
        DisciplinePartPersonSportKey,
        EventDisciplinePartKey,
        DisciplinePartPersonSport,
        EventPartPersonSportKey,
        EventPartPersonSport> {
    @Override
    public DisciplinePartPersonSportManager getManager(Statement stat) {
        return new DisciplinePartPersonSportManager(stat);
    }

    @Override
    public PersonSportManager getParticipantManager(Statement stat) {
        return new PersonSportManager(stat);
    }

    @Override
    public DisciplinePartPersonSportKey getKey(EventDisciplinePartKey partKey, int participantId) {
        EventPartPersonSportKey eppsKey = new EventPartPersonSportKey(partKey.getSuperKey(), participantId);

        return new DisciplinePartPersonSportKey(eppsKey, partKey.getEventDisciplinePartId());
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(EventDisciplinePartKey partKey) {
        return partKey.getSuperKey();
    }

    @Override
    public DisciplinePartPersonSport getInstance() {
        return new DisciplinePartPersonSport();
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
