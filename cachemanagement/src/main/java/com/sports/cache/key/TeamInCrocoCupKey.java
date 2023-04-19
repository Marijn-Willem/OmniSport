package com.sports.cache.key;

public class TeamInCrocoCupKey extends CacheKey {
    private final int teamId;

    public TeamInCrocoCupKey(int teamId) {
        this.teamId = teamId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(teamId);
    }
}
