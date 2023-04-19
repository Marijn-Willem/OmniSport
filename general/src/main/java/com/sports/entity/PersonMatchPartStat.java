package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class PersonMatchPartStat extends H2HMatchPartStat {
    private int personSportId;
    private Integer value2;

    private int personMatchPartId;
    private int personMatchPartStatId;

    @Override
    String[] getSpecificProperties() {
        return new String[] { "" + personSportId, QueryUtil.convertIntegerToDbValue(value2) };
    }

    public int getParticipantId() {
        return getPersonSportId();
    }

    public void setParticipantId(int participantId) {
        setPersonSportId(participantId);
    }

    public int getMatchPartId() {
        return getPersonMatchPartId();
    }

    public int getMatchPartStatId() {
        return getPersonMatchPartStatId();
    }

    public int getPersonSportId() {
        return personSportId;
    }

    public void setPersonSportId(int personSportId) {
        this.personSportId = personSportId;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        this.value2 = value2;
    }

    public int getPersonMatchPartId() {
        return personMatchPartId;
    }

    public void setPersonMatchPartId(int personMatchPartId) {
        this.personMatchPartId = personMatchPartId;
    }

    public int getPersonMatchPartStatId() {
        return personMatchPartStatId;
    }

    public void setPersonMatchPartStatId(int personMatchPartStatId) {
        this.personMatchPartStatId = personMatchPartStatId;
    }
}
