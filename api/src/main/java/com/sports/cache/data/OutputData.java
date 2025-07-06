package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class OutputData implements Serializable {
    protected static final int nestingLevel = 0;

    public abstract void fill(Statement stat) throws SQLException;
    public abstract boolean isValidOutput();
    public abstract CacheDataKey getCacheDataKey();
    public abstract String toXML();
    public abstract String toJson();
    public abstract String toYaml();
}
