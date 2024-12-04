package com.sports.entity;

import com.sports.entity.key.PersonSportIdKey;

public class PersonSport extends Participant {
    private int personId;
    private int sportId;

    private int teamId;
    private String sportName;
    private int genderId;

    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                String.valueOf(personId),
                String.valueOf(sportId)
        };
    }

    public PersonSportIdKey getPersonSportIdKey() {
        return new PersonSportIdKey(personId, sportId);
    }

    public void copyToForStanding(PersonSport other) {
        other.setPersonId(personId);
        other.setSportId(sportId);

        copyStandingFieldsTo(other);
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getPersonId() {
        return personId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }
}
