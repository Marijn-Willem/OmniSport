package com.sports.calc.alcifo;

import com.sports.entity.DisciplinePartPersonSport;
import com.sports.entity.PersonSport;
import com.sports.entity.key.*;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.entity.manager.DisciplinePartPersonSportManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.entity.manager.PersonSportManager;

import java.sql.Statement;

public class DisciplinePartPersonSportFactory implements AlcifoPartParticipantFactory {
    @Override
    public AlcifoPartParticipantManager<DisciplinePartPersonSportKey, EventDisciplinePartKey, DisciplinePartPersonSport> getManager(Statement stat) {
        return new DisciplinePartPersonSportManager(stat);
    }

    @Override
    public ParticipantManager<PersonSport> getParticipantManager(Statement stat) {
        return new PersonSportManager(stat);
    }

    @Override
    public DisciplinePartPersonSportKey getKey(SuperKey partKey, int participantId) {
        EventDisciplinePartKey edpKey = (EventDisciplinePartKey)partKey;
        EventPartPersonSportKey eppsKey = new EventPartPersonSportKey(edpKey.getSuperKey(), participantId);

        return new DisciplinePartPersonSportKey(eppsKey, edpKey.getEventDisciplinePartId());
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(SuperKey partKey) {
        return ((EventDisciplinePartKey)partKey).getSuperKey();
    }

    @Override
    public DisciplinePartPersonSport getInstance() {
        return new DisciplinePartPersonSport();
    }

    @Override
    public AlcifoParticipantFactory getParticipantFactory() {
        return new EventPersonSportFactory();
    }
}
