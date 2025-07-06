package com.flush.servlet.ajax;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.EntityInstanceCompSeasonKey;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class FlushEntityInstanceCompSeason extends FlushEntityInstanceNonCompSeason {
    @Override
    CacheFragmentKey getCacheKey(Statement stat, HttpServletRequest req) throws SQLException {
        String en = req.getParameter("en");
        String eas = req.getParameter("eas");
        Integer entityId = getEntityId(stat, en, eas);

        return entityId != null ? new EntityInstanceCompSeasonKey(en, entityId, compSeasonKey) : null;
    }
}
