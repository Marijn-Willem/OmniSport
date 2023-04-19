package com.sports.entity;

public class DoublesMatchPartStat extends H2HMatchPartStat {
    private int doubleId;

    private int doublesMatchPartId;
    private int doublesMatchPartStatId;

    String[] getSpecificProperties() {
        return new String[] { "" + doubleId };
    }

    public int getParticipantId() {
        return getDoubleId();
    }

    public void setParticipantId(int participantId) {
        setDoubleId(participantId);
    }

    public int getMatchPartId() {
        return getDoublesMatchPartId();
    }

    public int getMatchPartStatId() {
        return getDoublesMatchPartStatId();
    }

    public int getDoubleId() {
        return doubleId;
    }

    public void setDoubleId(int doubleId) {
        this.doubleId = doubleId;
    }

    public int getDoublesMatchPartId() {
        return doublesMatchPartId;
    }

    public void setDoublesMatchPartId(int doublesMatchPartId) {
        this.doublesMatchPartId = doublesMatchPartId;
    }

    public int getDoublesMatchPartStatId() {
        return doublesMatchPartStatId;
    }

    public void setDoublesMatchPartStatId(int doublesMatchPartStatId) {
        this.doublesMatchPartStatId = doublesMatchPartStatId;
    }
}
