package com.sports.entity.key;

public class EventPartTeamKey extends SuperKey {
    private final CompSeasonEventPartKey compSeasonEventPartKey;
    private final int teamId;

    public EventPartTeamKey(CompSeasonEventPartKey compSeasonEventPartKey, int teamId) {
        this.compSeasonEventPartKey = compSeasonEventPartKey;
        this.teamId = teamId;
    }

    @Override
    public int hashCode() {
        return 1000 * compSeasonEventPartKey.hashCode() + teamId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventPartTeamKey &&
                ((EventPartTeamKey)obj).compSeasonEventPartKey.equals(compSeasonEventPartKey) &&
                ((EventPartTeamKey)obj).teamId == teamId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonEventPartKey.getWhereClause() + " AND teamid = " + teamId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonEventPartKey.getSepValues(delim) + delim + teamId;
    }

    @Override
    public CompSeasonEventPartKey getSuperKey() {
        return compSeasonEventPartKey;
    }

    public int getTeamId() {
        return teamId;
    }
}
