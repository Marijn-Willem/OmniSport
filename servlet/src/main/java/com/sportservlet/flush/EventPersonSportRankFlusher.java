package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.entity.key.CompSeasonEventKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventPersonSportRankFlusher extends CacheFlusher {
    private final CompSeasonEventKey compSeasonEventKey;

    public EventPersonSportRankFlusher(CompSeasonEventKey compSeasonEventKey) {
        this.compSeasonEventKey = compSeasonEventKey;
    }

    @Override
    public List<CacheKey> generateCacheKeys(Statement stat) throws SQLException {
        return replicateForClientsWithRights(
                new EventPersonSportRankingReplicator(compSeasonEventKey.getCompSeasonEventId()),
                compSeasonEventKey.getSuperKey(), stat);
    }
}
