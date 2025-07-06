package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.util.CacheUtil;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class DataFragment {
    private CacheDataKey cacheDataKey;
    public abstract CacheFragmentKey getCacheFragmentKey();
    abstract void fill(Statement stat) throws SQLException;

    public void fill(CacheDataKey cacheDataKey, Statement stat) throws SQLException {
        this.cacheDataKey = cacheDataKey;
        CacheUtil.updateDataFragmentReferences(getCacheFragmentKey(), cacheDataKey);
        fill(stat);
    }

    CacheDataKey getCacheDataKey() {
        return cacheDataKey;
    }
}
