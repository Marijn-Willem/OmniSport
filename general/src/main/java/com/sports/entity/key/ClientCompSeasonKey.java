package com.sports.entity.key;

public class ClientCompSeasonKey extends SuperKey {
    private final int clientId;
    private final int competitionId;
    private final int seasonId;

    public ClientCompSeasonKey(int clientId, int competitionId, int seasonId) {
        this.clientId = clientId;
        this.competitionId = competitionId;
        this.seasonId = seasonId;
    }

    @Override
    public int hashCode() {
        return 10000 * clientId + 100 * competitionId + seasonId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClientCompSeasonKey &&
                ((ClientCompSeasonKey)obj).clientId == clientId &&
                ((ClientCompSeasonKey)obj).competitionId == competitionId &&
                ((ClientCompSeasonKey)obj).seasonId == seasonId;
    }

    public String getWhereClause() {
        return "clientid = " + clientId + " AND competitionid = " + competitionId +
                " AND seasonid = " + seasonId;
    }

    public String getSepValues(String delim) {
        return clientId + delim + competitionId + delim + seasonId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public CompSeasonKey getCompSeasonKey() {
        return new CompSeasonKey(competitionId, seasonId);
    }
}
