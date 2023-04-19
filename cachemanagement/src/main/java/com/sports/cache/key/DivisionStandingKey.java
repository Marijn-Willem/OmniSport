package com.sports.cache.key;

import com.sports.logic.util.Util;

public class DivisionStandingKey extends CacheDataKey {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonPhaseId;
    private final int compDivisionId;
    private final int clientId;

    public DivisionStandingKey(int competitionId, int seasonId, int compSeasonPhaseId, int compDivisionId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonPhaseId = compSeasonPhaseId;
        this.compDivisionId = compDivisionId;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] { Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(compSeasonPhaseId), Integer.toString(compDivisionId),
                Integer.toString(clientId) }, "|");
    }
}
