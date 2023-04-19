package com.sports.cache.util;

import com.sports.cache.data.CompetitionFragment;
import com.sports.cache.key.CacheDataKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ClientCompetitionFilter extends ClientEntityFilter<CompetitionFragment> {
    private final Set<Integer> competitionIds = new HashSet<>();

    public ClientCompetitionFilter(int clientId, CacheDataKey cacheDataKey, Statement stat) throws SQLException {
        super(clientId, cacheDataKey, stat);
    }

    @Override
    boolean isElementAllowedForNonAdmin(CompetitionFragment element) {
        return competitionIds.contains(element.getCompetitionId());
    }

    @Override
    void preProcess() {
        clientCompSeasonData.getCompSeasonFragments().forEach(x -> competitionIds.add(x.getCompetitionId()));
    }
}
