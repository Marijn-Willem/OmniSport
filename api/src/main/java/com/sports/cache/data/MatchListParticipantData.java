package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.MatchListParticipantKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MatchListParticipantData extends MatchListData {
	private final int participantId;

	@Override
	<MK extends H2HMatchKey, M extends H2HMatch> List<M> getH2HMatches(Statement stat, CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
			? extends CompSeasonPhaseParticipantKey,
			? extends Participant,
			? extends SuperKeyEntity,
			MK,
			M,
			? extends H2HMatchPartKey,
			? extends H2HMatchPart,
			? extends H2HMatchPartStatKey,
			? extends H2HMatchPartStat> factory, CompSeasonKey compSeasonKey)
			throws SQLException {
		CompSeasonParticipantKey compSeasonParticipantKey = factory.getCompSeasonParticKey(compSeasonKey, participantId);
		H2HMatchManager<MK, M> matchManager = factory.getH2HObjectFactory().getManager(stat);
		return matchManager.getMatchesParticipantInCompSeason(compSeasonParticipantKey);
	}

	@Override
	MatchListPhaseFragment getMatchListPhaseFragment(int competitionId, int seasonId, int compSeasonPhaseId,
													 List<H2HMatch> h2HMatches) {
		return new MatchListPhaseParticipantFragment(competitionId, seasonId, compSeasonPhaseId, h2HMatches,
				participantId, clientId, DataFragmentUtil.getLevelForNestedList(nestingLevel));
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
