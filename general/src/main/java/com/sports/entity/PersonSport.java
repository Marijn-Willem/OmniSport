package com.sports.entity;

import com.sports.entity.key.PersonSportIdKey;

import java.util.HashSet;
import java.util.Set;

public class PersonSport extends Participant {
    private int personId;
    private int sportId;

    private int goals;
    private int matches;
    private final Set<String> teamNames = new HashSet<String>();
    private int teamId;
    private String sportName;
    private int genderId;

    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                "" + personId,
                "" + sportId
        };
    }

    public PersonSportIdKey getPersonSportIdKey() {
        return new PersonSportIdKey(personId, sportId);
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

    public int getGoals() {
        return goals;
    }

    public void addGoal() {
        goals++;
    }

    public int getMatches() {
        return matches;
    }

    public void addMatch() {
        matches++;
    }

    public Set<String> getTeamNames() {
        return teamNames;
    }

    public void addTeamName(String teamName) {
        teamNames.add(teamName);
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
