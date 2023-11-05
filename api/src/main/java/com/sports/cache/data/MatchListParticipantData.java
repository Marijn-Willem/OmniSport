package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.MatchListParticipantKey;
import com.sports.entity.H2HMatch;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MatchListParticipantData extends MatchListData {
	private final int participantId;

	@Override
	List<H2HMatch> getH2HMatches(Statement stat, CompSeasonParticipantFactory factory, CompSeasonKey compSeasonKey)
			throws SQLException {
		CompSeasonParticipantKey compSeasonParticipantKey = factory.getCompSeasonParticKey(compSeasonKey, participantId);
		H2HMatchManager matchManager = factory.getH2HObjectFactory().getManager(stat);
		return matchManager.getMatchesParticipantInCompSeason(compSeasonParticipantKey);
	}

	@Override
	MatchListPhaseFragment getMatchListPhaseFragment(int competitionId, int seasonId, int compSeasonPhaseId,
													 List<H2HMatch> h2HMatches) {
		return new MatchListPhaseParticipantFragment(competitionId, seasonId, compSeasonPhaseId, h2HMatches,
				participantId, clientId);
	}

	public MatchListParticipantData(int competitionId, int seasonId, int participantId, Integer clientId) {
		super(competitionId, seasonId, clientId);
		this.participantId = participantId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new MatchListParticipantKey(competitionId, seasonId, participantId, clientId);
	}
}
