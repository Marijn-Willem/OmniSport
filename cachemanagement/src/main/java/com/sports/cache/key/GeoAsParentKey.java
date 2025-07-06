package com.sports.cache.key;

public class GeoAsParentKey extends CacheFragmentKey {
    private final int geoId;

    public GeoAsParentKey(int geoId) {
        this.geoId = geoId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(geoId);
    }
}
