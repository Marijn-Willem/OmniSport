package com.sports.entity.key;

public class CompSeasonTeamPersonSportKey extends SuperKey {
    private final CompSeasonTeamKey compSeasonTeamKey;
    private final int personSportId;

    public CompSeasonTeamPersonSportKey(CompSeasonTeamKey compSeasonTeamKey, int personSportId) {
        this.compSeasonTeamKey = compSeasonTeamKey;
        this.personSportId = personSportId;
    }

    @Override
    public int hashCode() {
        return 10000 * compSeasonTeamKey.hashCode() + personSportId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonTeamPersonSportKey &&
                ((CompSeasonTeamPersonSportKey)obj).compSeasonTeamKey.equals(compSeasonTeamKey) &&
                ((CompSeasonTeamPersonSportKey)obj).personSportId == personSportId;
    }

    @Override
    public CompSeasonTeamKey getSuperKey() {
        return compSeasonTeamKey;
    }

    public String getWhereClause() {
        return compSeasonTeamKey.getWhereClause() + " AND personsportid = " + personSportId;
    }

    public String getSepValues(String delim) {
        return compSeasonTeamKey.getSepValues(delim) + delim + personSportId;
    }

    public int getPersonSportId() {
        return personSportId;
    }
}
