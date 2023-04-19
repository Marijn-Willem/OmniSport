package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.GeoTypeKey;

import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class GeoTypeFlusher extends CacheFlusher {
    private final int geoTypeId;

    public GeoTypeFlusher(int geoTypeId) {
        this.geoTypeId = geoTypeId;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) {
        return Collections.singletonList(new GeoTypeKey(geoTypeId));
    }
}
