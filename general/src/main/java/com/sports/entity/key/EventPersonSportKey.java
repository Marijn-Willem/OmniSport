package com.sports.entity.key;

public class EventPersonSportKey extends AlcifoParticipantKey {
    @Override
    String getParticipantIdColumn() {
        return "personsportid";
    }

    public EventPersonSportKey(CompSeasonEventKey compSeasonEventKey, int personSportId) {
        super(compSeasonEventKey, personSportId);
    }

    public int getPersonSportId() {
        return getParticipantId();
    }
}
