package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.DivisionStandingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.CompDivision;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompDivisionManager;
import com.sports.entity.manager.CompSeasonPhaseManager;

import java.sql.SQLException;
import java.sql.Statement;

public class DivisionStandingData extends StandingData {
    private final int compDivisionId;

    public DivisionStandingData(int competitionId, int seasonId, int compSeasonPhaseId, int compDivisionId, Integer clientId) {
        super(competitionId, seasonId, compSeasonPhaseId, clientId);
        this.compDivisionId = compDivisionId;
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new DivisionStandingKey(competitionId, seasonId, compSeasonPhaseId, compDivisionId, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        CompSeasonPhaseKey compSeasonPhaseKey = getCompSeasonPhaseKey();
        CompDivisionKey compDivisionKey = getCompDivisionKey();

        CompSeasonPhase compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(compSeasonPhaseKey);
        CompDivision compDivision = new CompDivisionManager(stat).getCompDivision(compDivisionKey);

        if (compSeasonPhase != null && compSeasonPhase.isHasDivisionStandings() && compDivision != null) {
            boolean isDomesticUSA = isDomesticUSA(stat);
            new DbCalculation(stat).getDivisionStanding(compSeasonPhaseKey, compDivisionKey).forEach(x ->
                standingParticipants.add(
                        new StandingParticipantFragment(compSeasonPhaseKey.getSuperKey(), x, clientId, isDomesticUSA)
                )
            );

            DataFragmentUtil.fillDataFragments(standingParticipants, getCacheKey());
        }
    }

    private CompDivisionKey getCompDivisionKey() {
        return new CompDivisionKey(competitionId, compDivisionId);
    }
}
