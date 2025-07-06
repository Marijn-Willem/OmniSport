package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonPhaseStandingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.logic.calculation.DbCalculation;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPhaseStandingData extends StandingData {
    public CompSeasonPhaseStandingData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
        super(competitionId, seasonId, compSeasonPhaseId, clientId);
    }

    @Override
    public CacheDataKey getCacheDataKey() {
        return new CompSeasonPhaseStandingKey(competitionId, seasonId, compSeasonPhaseId, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        if (isCompSeasonPhaseWithStanding(stat)) {
            CompSeasonPhaseKey compSeasonPhaseKey = getCompSeasonPhaseKey();
            boolean isDomesticUSA = isDomesticUSA(stat);

            new DbCalculation(stat).getParticipantStandingCompSeasonPhase(compSeasonPhaseKey).forEach(x ->
                standingParticipants.add(
                        new StandingParticipantFragment(compSeasonPhaseKey.getSuperKey(), x, clientId, nestingLevelList, isDomesticUSA)
                )
            );

            DataFragmentUtil.fillDataFragments(standingParticipants, getCacheDataKey());
        }
    }
}
