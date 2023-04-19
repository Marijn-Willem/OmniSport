package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.db.execute.DatabaseExecutor;
import com.sports.logic.async.ThreadWorker;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class CacheFlusher extends DatabaseExecutor implements ThreadWorker {
    protected abstract List<CacheKey> getCacheKeys(Statement stat) throws SQLException;

    @Override
    public void doWork(Statement stat) throws SQLException {
        getCacheKeys(stat).forEach(CacheKey::delete);
    }

    @Override
    public void doWork() {
        execute();
    }
}
