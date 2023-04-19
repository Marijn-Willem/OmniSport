package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheKey;
import com.sports.cache.util.CacheUtil;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class DataFragment {
    private CacheDataKey cacheDataKey;
    public abstract CacheKey getCacheKey();
    abstract void fill(Statement stat) throws SQLException;

    public void fill(CacheDataKey cacheDataKey, Statement stat) throws SQLException {
        this.cacheDataKey = cacheDataKey;
        CacheUtil.updateDataFragmentReferences(getCacheKey().getStringRepresentation(), cacheDataKey);
        fill(stat);
    }

    CacheDataKey getCacheDataKey() {
        return cacheDataKey;
    }
}
