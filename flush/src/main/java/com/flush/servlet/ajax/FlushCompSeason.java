package com.flush.servlet.ajax;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CompSeasonKey;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class FlushCompSeason extends Flush {
    @Override
    CacheFragmentKey getCacheKey(Statement stat, HttpServletRequest req) {
        return new CompSeasonKey(competitionId, seasonId);
    }
}
