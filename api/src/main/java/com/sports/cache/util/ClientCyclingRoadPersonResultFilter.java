package com.sports.cache.util;

import com.sports.cache.data.CyclingRoadPersonResultFragment;
import com.sports.cache.key.CacheDataKey;

import java.sql.SQLException;
import java.sql.Statement;

public class ClientCyclingRoadPersonResultFilter extends ClientEntityFilter<CyclingRoadPersonResultFragment> {
    public ClientCyclingRoadPersonResultFilter(int clientId, CacheDataKey cacheDataKey, Statement stat) throws SQLException {
        super(clientId, cacheDataKey, stat);
    }

    @Override
    boolean isElementAllowedForNonAdmin(CyclingRoadPersonResultFragment element) {
        return clientCompSeasonData.hasCompSeason(element.getCompSeasonKey());
    }

    @Override
    void preProcess() {

    }
}
