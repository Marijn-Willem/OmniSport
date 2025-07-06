package com.sports.cache.key;

public class GeoTypeKey extends CacheFragmentKey {
    private final int geoTypeId;

    public GeoTypeKey(int geoTypeId) {
        this.geoTypeId = geoTypeId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(geoTypeId);
    }
}
