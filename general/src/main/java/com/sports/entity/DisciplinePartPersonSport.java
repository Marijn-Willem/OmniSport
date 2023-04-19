package com.sports.entity;

public class DisciplinePartPersonSport extends AlcifoPartParticipant {
    private int eventDisciplinePartId;
    private int personSportId;

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[0];
    }

    @Override
    public int getParticipantId() {
        return getPersonSportId();
    }

    @Override
    public void setParticipantId(int participantId) {
        setPersonSportId(participantId);
    }

    public int getEventDisciplinePartId() {
        return eventDisciplinePartId;
    }

    public void setEventDisciplinePartId(int eventDisciplinePartId) {
        this.eventDisciplinePartId = eventDisciplinePartId;
    }

    public int getPersonSportId() {
        return personSportId;
    }

    public void setPersonSportId(int personSportId) {
        this.personSportId = personSportId;
    }
}
