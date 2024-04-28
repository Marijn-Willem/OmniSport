package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class PersonMatchPart extends H2HMatchPart {
    private boolean person1Win;

    private int personMatchPartId;

    public int getMatchPartId() {
        return getPersonMatchPartId();
    }

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] { QueryUtil.convertBooleanToDbValue(person1Win)} ;
    }

    @Override
    void copySpecific(H2HMatchPart other) {
        ((PersonMatchPart) other).person1Win = person1Win;
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
}
