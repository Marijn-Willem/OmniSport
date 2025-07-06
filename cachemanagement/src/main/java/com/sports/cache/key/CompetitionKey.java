package com.sports.cache.key;

public class CompetitionKey extends CacheFragmentKey {
    private final int competitionId;

    public CompetitionKey(int competitionId) {
        this.competitionId = competitionId;
    }

    @Override
    public String getSpecificKeyPart() {
        return Integer.toString(competitionId);
    }
}
