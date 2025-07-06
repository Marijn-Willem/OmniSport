package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompetitionKey;

import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class CompetitionFlusher extends CacheFlusher {
    private final int competitionId;

    public CompetitionFlusher(int competitionId) {
        this.competitionId = competitionId;
    }

    @Override
    public List<CacheKey> generateCacheKeys(Statement stat) {
        return Collections.singletonList(new CompetitionKey(competitionId));
    }
}
