package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.entity.key.CompSeasonKey;

import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class CompSeasonKeyFlusher extends CacheFlusher {
    private final CompSeasonKey compSeasonKey;

    public CompSeasonKeyFlusher(CompSeasonKey compSeasonKey) {
        this.compSeasonKey = compSeasonKey;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) {
        return Collections.singletonList(new com.sports.cache.key.CompSeasonKey(
                compSeasonKey.getCompetitionId(), compSeasonKey.getSeasonId()));
    }
}
