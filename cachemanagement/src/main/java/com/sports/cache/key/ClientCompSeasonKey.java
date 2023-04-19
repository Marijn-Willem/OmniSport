package com.sports.cache.key;

public class ClientCompSeasonKey extends CacheDataKey {
    private final int clientId;

    public ClientCompSeasonKey(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getSpecificKeyPart() {
        return Integer.toString(clientId);
    }

    @Override
    public CacheKey getMetaKey() {
        return new ClientCompSeasonMetaKey(clientId);
    }
}
