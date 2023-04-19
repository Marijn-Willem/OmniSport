package com.sports.cache.key;

public class GeoKey extends CacheKey {
    private final int geoId;

    public GeoKey(int geoId) {
        this.geoId = geoId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(geoId);
    }
}
