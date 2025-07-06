package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.GeoAsParentKey;
import com.sports.cache.key.GeoKey;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GeoFlusher extends CacheFlusher {
    private final int geoId;

    public GeoFlusher(int geoId) {
        this.geoId = geoId;
    }

    @Override
    public List<CacheKey> generateCacheKeys(Statement stat) {
        return new ArrayList<>() {{
            add(new GeoKey(geoId));
            add(new GeoAsParentKey(geoId));
        }};
    }
}
