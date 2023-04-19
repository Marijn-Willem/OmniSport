package com.sports.entity.key;

public class CompSeasonPhaseKey extends SuperKey implements EntityKeyWithParent {
    private final CompSeasonKey compSeasonKey;
    private final int compSeasonPhaseId;

    public CompSeasonPhaseKey(CompSeasonKey csk, int compSeasonPhaseId) {
        this.compSeasonKey = csk;
        this.compSeasonPhaseId = compSeasonPhaseId;
    }

    @Override
    public int hashCode() {
        return 100 * compSeasonKey.hashCode() + compSeasonPhaseId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonPhaseKey &&
                ((CompSeasonPhaseKey)obj).getSuperKey().equals(compSeasonKey) &&
                ((CompSeasonPhaseKey)obj).getCompSeasonPhaseId() == compSeasonPhaseId;
    }

    public String getWhereClauseParent() {
        return compSeasonKey.getWhereClause() +  " AND parentphaseid = " + compSeasonPhaseId;
    }

    public String getWhereClause() {
        return compSeasonKey.getWhereClause() + " AND compseasonphaseid = " + compSeasonPhaseId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonKey.getSepValues(delim) + delim + compSeasonPhaseId;
    }

    @Override
    public CompSeasonKey getSuperKey() {
        return compSeasonKey;
    }

    public int getCompSeasonPhaseId() {
        return compSeasonPhaseId;
    }

    public int getCompetitionId() {
        return compSeasonKey.getCompetitionId();
    }

    public int getSeasonId() {
        return compSeasonKey.getSeasonId();
    }
}
