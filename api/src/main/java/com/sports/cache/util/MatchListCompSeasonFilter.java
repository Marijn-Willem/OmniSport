package com.sports.cache.util;

import com.sports.cache.data.MatchListCompSeasonFragment;
import com.sports.cache.key.CacheDataKey;
import com.sports.entity.key.CompSeasonKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class MatchListCompSeasonFilter extends ClientEntityFilter<MatchListCompSeasonFragment> {
    private final Set<CompSeasonKey> compSeasonKeys = new HashSet<>();

    public MatchListCompSeasonFilter(int clientId, CacheDataKey cacheDataKey, Statement stat) throws SQLException {
        super(clientId, cacheDataKey, stat);
    }

    @Override
    boolean isElementAllowedForNonAdmin(MatchListCompSeasonFragment element) {
        return compSeasonKeys.contains(element.getCompSeasonKey());
    }

    @Override
    void preProcess() {
        clientCompSeasonData.getCompSeasonFragments().forEach(x -> compSeasonKeys.add(x.getCompSeasonKey()));
    }
}
