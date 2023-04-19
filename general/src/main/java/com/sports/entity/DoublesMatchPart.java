package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.DoublesMatchPartKey;
import com.sports.entity.key.DoublesMatchPartStatKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.manager.DoublesMatchPartStatManager;

import java.sql.Statement;

public class DoublesMatchPart extends H2HMatchPart {
    private boolean double1Win;

    private int doublesMatchPartId;

    public int getMatchPartId() {
        return getDoublesMatchPartId();
    }

    public DoublesMatchPartStatManager getStatManager(Statement stat) {
        return new DoublesMatchPartStatManager(stat);
    }

    public DoublesMatchPartStatKey getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId) {
        return new DoublesMatchPartStatKey((DoublesMatchPartKey)h2HMatchPartKey, specifId);
    }

    public DoublesMatchPartStat getMatchPartStat() {
        return new DoublesMatchPartStat();
    }

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] { QueryUtil.convertBooleanToDbValue(double1Win) };
    }

    public boolean getParticipant1Win() {
        return isDouble1Win();
    }

    public void setParticipant1Win(boolean participant1Win) {
        setDouble1Win(participant1Win);
    }

    public boolean isDouble1Win() {
        return double1Win;
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
