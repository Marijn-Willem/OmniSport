package com.flush.servlet.ajax;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.SportKey;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class FlushSport extends Flush {
    @Override
    CacheFragmentKey getCacheKey(Statement stat, HttpServletRequest req) {
        return new SportKey(getIntValuedParameterValue(req, "spid"));
    }
}
