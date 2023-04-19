package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class OutputData implements Serializable {
    public abstract void fill(Statement stat) throws SQLException;
    public abstract boolean isValidOutput();
    public abstract CacheDataKey getCacheKey();
    public abstract String toXML();
    public abstract String toJson();
}
