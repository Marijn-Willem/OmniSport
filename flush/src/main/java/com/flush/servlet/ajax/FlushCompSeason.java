package com.flush.servlet.ajax;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompSeasonKey;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class FlushCompSeason extends Flush {
    @Override
    CacheKey getCacheKey(Statement stat, HttpServletRequest req) {
        return new CompSeasonKey(competitionId, seasonId);
    }
}
