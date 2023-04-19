package com.sports.entity.key;

public abstract class AlcifoParticipantKey extends SuperKey {
    private final CompSeasonEventKey compSeasonEventKey;
    private final int participantId;

    abstract String getParticipantIdColumn();

    public AlcifoParticipantKey(CompSeasonEventKey compSeasonEventKey, int participantId) {
        this.compSeasonEventKey = compSeasonEventKey;
        this.participantId = participantId;
    }

    @Override
    public int hashCode() {
        return 1000 * compSeasonEventKey.hashCode() + participantId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AlcifoParticipantKey &&
                ((AlcifoParticipantKey)obj).compSeasonEventKey.equals(compSeasonEventKey) &&
                ((AlcifoParticipantKey)obj).participantId == participantId;
    }

    @Override
    public CompSeasonEventKey getSuperKey() {
        return compSeasonEventKey;
    }

    @Override
    public String getWhereClause() {
        return compSeasonEventKey.getWhereClause() + " AND " + getParticipantIdColumn() + " = " + participantId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonEventKey.getSepValues(delim) + delim + participantId;
    }

    public CompSeasonEventKey getCompSeasonEventKey() {
        return compSeasonEventKey;
    }

    public int getParticipantId() {
        return participantId;
    }
}
