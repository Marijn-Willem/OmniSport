package com.sports.calc.alcifo;

import com.sports.entity.EventPartPersonSport;
import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartPersonSportKey;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.entity.manager.PersonSportManager;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class EventPartPersonSportFactory implements AlcifoPartParticipantFactory {
    @Override
    public AlcifoPartParticipantManager<EventPartPersonSportKey, CompSeasonEventPartKey, EventPartPersonSport> getManager(Statement stat) {
        return new EventPartPersonSportManager(stat);
    }

    @Override
    public ParticipantManager<PersonSport> getParticipantManager(Statement stat) {
        return new PersonSportManager(stat);
    }

    @Override
    public Map<EventPartPersonSportKey, EventPartPersonSport> getEmptyMap() {
        return new HashMap<>();
    }

    @Override
    public EventPartPersonSportKey getKey(SuperKey partKey, int participantId) {
        return new EventPartPersonSportKey((CompSeasonEventPartKey)partKey, participantId);
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(SuperKey partKey) {
        return (CompSeasonEventPartKey)partKey;
    }

    @Override
    public EventPartPersonSport getInstance() {
        return new EventPartPersonSport();
    }

    @Override
    public AlcifoParticipantFactory getParticipantFactory() {
        return new EventPersonSportFactory();
    }
}
