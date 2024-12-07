package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.DivisionStandingEvolutionKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.key.CompSeasonPhaseKey;

import java.sql.SQLException;
import java.sql.Statement;

public class DivisionStandingEvolutionData extends StandingEvolutionData {
	private final int compDivisionId;

	public DivisionStandingEvolutionData(int competitionId, int seasonId, int compSeasonPhaseId, int compDivisionId, Integer clientId) {
		super(competitionId, seasonId, compSeasonPhaseId, clientId);
		this.compDivisionId = compDivisionId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new DivisionStandingEvolutionKey(competitionId, seasonId, compSeasonPhaseId, compDivisionId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		if (isCompDivisionWithStanding(compDivisionId, stat)) {
			CompSeasonPhaseKey compSeasonPhaseKey = getCompSeasonPhaseKey();
			CompDivisionKey compDivisionKey = new CompDivisionKey(competitionId, compDivisionId);
			boolean isDomesticUSA = isDomesticUSA(stat);

			new DbCalculation(stat).getDivisionStandingPerDate(compSeasonPhaseKey, compDivisionKey).forEach(x ->
					snapshotFragments.add(
							new StandingEvolutionSnapshotFragment(competitionId, seasonId, compSeasonPhaseId, clientId, isDomesticUSA, x)
					)
			);

			DataFragmentUtil.fillDataFragments(snapshotFragments, getCacheKey());
		}
	}
}
