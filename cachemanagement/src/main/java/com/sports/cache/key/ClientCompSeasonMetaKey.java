package com.sports.cache.key;

public class ClientCompSeasonMetaKey extends CacheKey {
    private final int clientId;

    public ClientCompSeasonMetaKey(int clientId) {
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(clientId);
    }
}
