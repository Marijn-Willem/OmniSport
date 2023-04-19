package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonPhaseStandingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.logic.calculation.DbCalculation;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPhaseStandingData extends StandingData {
    public CompSeasonPhaseStandingData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
        super(competitionId, seasonId, compSeasonPhaseId, clientId);
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new CompSeasonPhaseStandingKey(competitionId, seasonId, compSeasonPhaseId, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        CompSeasonPhaseKey compSeasonPhaseKey = getCompSeasonPhaseKey();
        CompSeasonPhase compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(compSeasonPhaseKey);

        if (compSeasonPhase != null && compSeasonPhase.isHasStanding()) {
            boolean isDomesticUSA = isDomesticUSA(stat);
            new DbCalculation(stat).getParticipantStandingCompSeasonPhase(compSeasonPhaseKey).forEach(x ->
                standingParticipants.add(
                        new StandingParticipantFragment(compSeasonPhaseKey.getSuperKey(), x, clientId, isDomesticUSA)
                )
            );

            DataFragmentUtil.fillDataFragments(standingParticipants, getCacheKey());
        }
    }
}
