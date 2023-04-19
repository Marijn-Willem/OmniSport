package com.sports.cache.key;

public class SportListKey extends CacheDataKey {
    private final int clientId;

    public SportListKey(int clientId) {
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(clientId);
    }
}
