package com.sports.entity.key;

public class CompDivisionKey extends SuperKey implements EntityKeyWithParent {
    private int competitionId;
    private int compDivisionId;

    public CompDivisionKey(int competitionId, int compDivisionId) {
        this.competitionId = competitionId;
        this.compDivisionId = compDivisionId;
    }

    @Override
    public int hashCode() {
        return 100 * competitionId + compDivisionId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompDivisionKey &&
                ((CompDivisionKey)obj).competitionId == competitionId &&
                ((CompDivisionKey)obj).compDivisionId == compDivisionId;
    }

    public String getWhereClause() {
        return "competitionid = " + competitionId + " AND compdivisionid = " + compDivisionId;
    }

    public String getWhereClauseParent() {
        return "competitionid = " + competitionId + " AND parentdivisionid = " + compDivisionId;
    }

    public String getSepValues(String delim) {
        return competitionId + delim + compDivisionId;
    }

    public int getCompDivisionId() {
        return compDivisionId;
    }
}
