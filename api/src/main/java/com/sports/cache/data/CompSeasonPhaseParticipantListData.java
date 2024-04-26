package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonPhaseParticipantListKey;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonPhaseParticipantListData extends ParticipantListData {
	private final int compSeasonPhaseId;

	public CompSeasonPhaseParticipantListData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
		super(competitionId, seasonId, clientId);
		this.compSeasonPhaseId = compSeasonPhaseId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new CompSeasonPhaseParticipantListKey(competitionId, seasonId, compSeasonPhaseId, clientId);
	}

	@Override
	List<Integer> getParticipantIds(Statement stat, CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
			? extends CompSeasonPhaseParticipantKey,
			? extends Participant,
			? extends SuperKeyEntity,
			? extends SuperKeyEntity,
			? extends H2HMatchKey,
			? extends H2HMatch,
			? extends H2HMatchPartKey,
			? extends H2HMatchPart,
			? extends H2HMatchPartStatKey,
			? extends H2HMatchPartStat> factory) throws SQLException {
		CompSeasonPhaseKey compSeasonPhaseKey = new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId),
				compSeasonPhaseId);

		return factory.getPhaseParticManager(stat).getParticipantIds(compSeasonPhaseKey);
	}
}
