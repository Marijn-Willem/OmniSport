package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.SportKey;

import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class SportFlusher extends CacheFlusher {
    private final int sportId;

    public SportFlusher(int sportId) {
        this.sportId = sportId;
    }

    @Override
    public List<CacheKey> generateCacheKeys(Statement stat) {
        return Collections.singletonList(new SportKey(sportId));
    }
}
