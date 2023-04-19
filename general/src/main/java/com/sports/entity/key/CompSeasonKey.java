package com.sports.entity.key;

public class CompSeasonKey extends SuperKey {
    private final int competitionId;
    private final int seasonId;

    public CompSeasonKey(int competitionId, int seasonId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    @Override
    public int hashCode() {
        return 100 * seasonId + competitionId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonKey &&
                ((CompSeasonKey)obj).competitionId == competitionId &&
                ((CompSeasonKey)obj).seasonId == seasonId;
    }

    public String getSepValues(String delim) {
        return competitionId + delim + seasonId;
    }

    @Override
    public String getWhereClause() {
        return "competitionid = " + competitionId + " AND seasonid = " + seasonId;
    }
}
