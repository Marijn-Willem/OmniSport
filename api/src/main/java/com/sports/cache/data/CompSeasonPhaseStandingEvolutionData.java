package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonPhaseStandingEvolutionKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.logic.calculation.DbCalculation;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPhaseStandingEvolutionData extends StandingEvolutionData {
	public CompSeasonPhaseStandingEvolutionData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
		super(competitionId, seasonId, compSeasonPhaseId, clientId);
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new CompSeasonPhaseStandingEvolutionKey(competitionId, seasonId, compSeasonPhaseId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		if (isCompSeasonPhaseWithStanding(stat)) {
			boolean isDomesticUSA = isDomesticUSA(stat);

			new DbCalculation(stat).getParticipantStandingCompSeasonPhasePerDate(getCompSeasonPhaseKey())
					.forEach(x -> snapshotFragments.add(
							new StandingEvolutionSnapshotFragment(competitionId, seasonId, compSeasonPhaseId, clientId, isDomesticUSA, x)
						)
					);

			DataFragmentUtil.fillDataFragments(snapshotFragments, getCacheKey());
		}
	}
}
