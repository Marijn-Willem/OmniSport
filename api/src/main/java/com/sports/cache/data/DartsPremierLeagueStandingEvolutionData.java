package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.DartsPremierLeagueStandingEvolutionKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.calc.darts.DbCalculation;
import com.sports.entity.Competition;

import java.sql.SQLException;
import java.sql.Statement;

public class DartsPremierLeagueStandingEvolutionData extends StandingEvolutionData {
	public DartsPremierLeagueStandingEvolutionData(int seasonId, Integer clientId) {
		super(Competition.competitionIdDartsPremierLeague, seasonId, 0, clientId);
	}

	@Override
	public CacheDataKey getCacheDataKey() {
		return new DartsPremierLeagueStandingEvolutionKey(seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		new DbCalculation(stat).getPremierLeagueStandingPerDate(seasonId).forEach(context ->
				snapshotFragments.add(new StandingEvolutionSnapshotFragment(
						competitionId, seasonId, 0, clientId, nestingLevelList, false, context)
				));

		DataFragmentUtil.fillDataFragments(snapshotFragments, getCacheDataKey());
	}
}
