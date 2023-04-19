package com.sports.entity.key;

public class CompSeasonEventPartKey extends SuperKey {
    private CompSeasonEventKey compSeasonEventKey;
    private int compSeasonEventPartId;

    public CompSeasonEventPartKey(CompSeasonEventKey csek, int compSeasonEventPartId) {
        this.compSeasonEventKey = csek;
        this.compSeasonEventPartId = compSeasonEventPartId;
    }

    @Override
    public int hashCode() {
        return 100 * getSuperKey().hashCode() + compSeasonEventPartId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonEventPartKey &&
                ((CompSeasonEventPartKey)obj).getSuperKey().equals(compSeasonEventKey) &&
                ((CompSeasonEventPartKey)obj).getCompSeasonEventPartId() == compSeasonEventPartId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonEventKey.getSepValues(delim) + delim + compSeasonEventPartId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonEventKey.getWhereClause() + " AND compseasoneventpartid = " + compSeasonEventPartId;
    }

    @Override
    public CompSeasonEventKey getSuperKey() {
        return compSeasonEventKey;
    }

    public int getCompSeasonEventPartId() {
        return compSeasonEventPartId;
    }
}
