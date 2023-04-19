package com.sports.cache.key;

public class ClientKey extends CacheKey {
    private final int clientId;

    public ClientKey(int clientId) {
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(clientId);
    }
}
