package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class DoublesMatchPart extends H2HMatchPart {
    private boolean double1Win;

    private int doublesMatchPartId;

    public int getMatchPartId() {
        return getDoublesMatchPartId();
    }

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] { QueryUtil.convertBooleanToDbValue(double1Win) };
    }

    @Override
    void copySpecific(H2HMatchPart other) {
        ((DoublesMatchPart) other).double1Win = double1Win;
    }

    public void setParticipant1Win(boolean participant1Win) {
        setDouble1Win(participant1Win);
    }

    public void setDouble1Win(boolean double1Win) {
        this.double1Win = double1Win;
    }

    public int getDoublesMatchPartId() {
        return doublesMatchPartId;
    }

    public void setDoublesMatchPartId(int doublesMatchPartId) {
        this.doublesMatchPartId = doublesMatchPartId;
    }
}
