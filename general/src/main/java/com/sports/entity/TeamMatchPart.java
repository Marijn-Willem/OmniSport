package com.sports.entity;

import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartStatManager;

import java.sql.Statement;

public class TeamMatchPart extends H2HMatchPart {
    private int teamMatchPartId;

    public int getMatchPartId() {
        return teamMatchPartId;
    }

    public H2HMatchPartStatManager getStatManager(Statement stat) {
        return null;
    }

    public H2HMatchPartStatKey getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId) {
        return null;
    }

    public H2HMatchPartStat getMatchPartStat() {
        return null;
    }

    String[] getSpecificPropertiesInSQLStrings() {
        return new String[0];
    }

    public boolean getParticipant1Win() {
        return false;
    }

    public void setParticipant1Win(boolean participant1Win) {

    }

    public int getTeamMatchPartId() {
        return teamMatchPartId;
    }

    public void setTeamMatchPartId(int teamMatchPartId) {
        this.teamMatchPartId = teamMatchPartId;
    }
}
