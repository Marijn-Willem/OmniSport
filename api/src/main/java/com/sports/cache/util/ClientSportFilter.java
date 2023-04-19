package com.sports.cache.util;

import com.sports.cache.data.CompSeasonFragment;
import com.sports.cache.data.CompetitionFragment;
import com.sports.cache.data.SportFragment;
import com.sports.cache.key.CacheDataKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientSportFilter extends ClientEntityFilter<SportFragment> {
    private final Set<Integer> sportIds = new HashSet<>();

    public ClientSportFilter(int clientId, CacheDataKey cacheDataKey, Statement stat) throws SQLException {
        super(clientId, cacheDataKey, stat);
    }

    @Override
    boolean isElementAllowedForNonAdmin(SportFragment element) {
        return sportIds.contains(element.getSportId());
    }

    @Override
    void preProcess() {
        Set<CompSeasonFragment> compSeasonFragments = clientCompSeasonData.getCompSeasonFragments();
        Set<Integer> competitionIds = new HashSet<>() {{
            compSeasonFragments.forEach(x -> add(x.getCompetitionId()));
        }};

        List<CompetitionFragment> competitionFragments = competitionIds.stream().map(
                CompetitionFragment::new).collect(Collectors.toList());

        DataFragmentUtil.fillDataFragments(competitionFragments, cacheDataKey);

        competitionFragments.forEach(x -> sportIds.add(x.getSportId()));
    }
}
