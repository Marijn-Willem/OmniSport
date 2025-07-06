package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.TeamInCrocoCupKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class TeamFlusher extends CacheFlusher {
    private final int teamId;

    public TeamFlusher(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public List<CacheKey> generateCacheKeys(Statement stat) throws SQLException {
        return Collections.singletonList(new TeamInCrocoCupKey(teamId));
    }
}
