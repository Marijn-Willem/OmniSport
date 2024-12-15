package com.sports.entity.key;

public class CompSeasonPhaseTeamCorrectionKey extends SuperKey {
    private final CompSeasonPhaseTeamKey compSeasonPhaseTeamKey;
    private final int compSeasonPhaseTeamCorrectionId;

    public CompSeasonPhaseTeamCorrectionKey(CompSeasonPhaseTeamKey compSeasonPhaseTeamKey, int compSeasonPhaseTeamCorrectionId) {
        this.compSeasonPhaseTeamKey = compSeasonPhaseTeamKey;
        this.compSeasonPhaseTeamCorrectionId = compSeasonPhaseTeamCorrectionId;
    }

    @Override
    public int hashCode() {
        return 100 * compSeasonPhaseTeamKey.hashCode() + compSeasonPhaseTeamCorrectionId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonPhaseTeamCorrectionKey &&
                ((CompSeasonPhaseTeamCorrectionKey)obj).compSeasonPhaseTeamKey.equals(compSeasonPhaseTeamKey) &&
                ((CompSeasonPhaseTeamCorrectionKey)obj).compSeasonPhaseTeamCorrectionId == compSeasonPhaseTeamCorrectionId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonPhaseTeamKey.getWhereClause() + " AND compseasonphasecorrectionid = " +
                compSeasonPhaseTeamCorrectionId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonPhaseTeamKey.getSepValues(delim) + delim + compSeasonPhaseTeamCorrectionId;
    }

    @Override
    public CompSeasonPhaseTeamKey getSuperKey() {
        return compSeasonPhaseTeamKey;
    }

    public int getCompSeasonPhaseTeamCorrectionId() {
        return compSeasonPhaseTeamCorrectionId;
    }
}
