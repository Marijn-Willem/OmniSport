package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.DartsPremierLeagueStandingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.calc.darts.DbCalculation;
import com.sports.entity.Competition;
import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.calculation.StandingContext;

import java.sql.SQLException;
import java.sql.Statement;

public class DartsPremierLeagueStandingData extends StandingData {
	public DartsPremierLeagueStandingData(int seasonId, Integer clientId) {
		super(Competition.competitionIdDartsPremierLeague, seasonId, 0, clientId);
	}

	@Override
	public CacheDataKey getCacheDataKey() {
		return new DartsPremierLeagueStandingKey(seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(Competition.competitionIdDartsPremierLeague, seasonId);

		StandingContext<PersonSport> standingContext = new DbCalculation(stat).getPremierLeagueStanding(seasonId);

		if (standingContext != null)
			standingContext.standing().forEach(x -> standingParticipants.add(
					new StandingParticipantFragment(compSeasonKey, x, clientId,
							nestingLevelList, false)));

		DataFragmentUtil.fillDataFragments(standingParticipants, getCacheDataKey());
	}
}
