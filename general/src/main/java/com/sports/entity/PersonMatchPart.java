package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.key.PersonMatchPartStatKey;
import com.sports.entity.manager.PersonMatchPartStatManager;

import java.sql.Statement;

public class PersonMatchPart extends H2HMatchPart {
    private boolean person1Win;

    private int personMatchPartId;
    private int person1Score;
    private int person2Score;

    public int getMatchPartId() {
        return getPersonMatchPartId();
    }

    public PersonMatchPartStatManager getStatManager(Statement stat) {
        return new PersonMatchPartStatManager(stat);
    }

    public PersonMatchPartStatKey getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId) {
        return new PersonMatchPartStatKey((PersonMatchPartKey)h2HMatchPartKey, specifId);
    }

    public PersonMatchPartStat getMatchPartStat() {
        return new PersonMatchPartStat();
    }

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] { QueryUtil.convertBooleanToDbValue(person1Win)} ;
    }

    public boolean getParticipant1Win() {
        return isPerson1Win();
    }

    public void setParticipant1Win(boolean participant1Win) {
        setPerson1Win(participant1Win);
    }

    public boolean isPerson1Win() {
        return person1Win;
    }

    public void setPerson1Win(boolean person1Win) {
        this.person1Win = person1Win;
    }

    public int getPersonMatchPartId() {
        return personMatchPartId;
    }

    public void setPersonMatchPartId(int personMatchPartId) {
        this.personMatchPartId = personMatchPartId;
    }

    public int getPerson1Score() {
        return person1Score;
    }

    public void setPerson1Score(int person1Score) {
        this.person1Score = person1Score;
    }

    public int getPerson2Score() {
        return person2Score;
    }

    public void setPerson2Score(int person2Score) {
        this.person2Score = person2Score;
    }
}
