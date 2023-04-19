package com.sports.entity.key;

public class CompSeasonDivisionKey extends SuperKey {
    private CompSeasonKey compSeasonKey;
    private int compDivisionId;

    public CompSeasonDivisionKey(CompSeasonKey compSeasonKey, int compDivisionId) {
        this.compSeasonKey = compSeasonKey;
        this.compDivisionId = compDivisionId;
    }

    @Override
    public int hashCode() {
        return 100 * getSuperKey().hashCode() + compDivisionId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonDivisionKey &&
                ((CompSeasonDivisionKey)obj).getSuperKey().equals(compSeasonKey) &&
                ((CompSeasonDivisionKey)obj).compDivisionId == compDivisionId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonKey.getSepValues(delim) + delim + compDivisionId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonKey.getWhereClause() + " AND compdivisionid = " + compDivisionId;
    }

    @Override
    public CompSeasonKey getSuperKey() {
        return compSeasonKey;
    }

    public int getCompDivisionId() {
        return compDivisionId;
    }
}
