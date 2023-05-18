package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.SpeedSkatingTotalRankingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.speedskating.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.CompSeasonEventManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SpeedSkatingTotalRankingData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final int compSeasonEventPartId;
	private final Integer clientId;

	private final List<SpeedSkatingRankingPersonFragment> rankingPersonFragments = new ArrayList<>();

	public SpeedSkatingTotalRankingData(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId,
										Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.compSeasonEventPartId = compSeasonEventPartId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new SpeedSkatingTotalRankingKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonEventKey compSeasonEventKey = new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId),
				compSeasonEventId);

		CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(compSeasonEventKey);

		if (compSeasonEvent != null) {
			SportEventKey sek = compSeasonEvent.getSportEventKey();

			if (sek.getSportId() == Sport.sportIdSpeedSkating && (
					sek.getSportEventId() == SportEvent.sportEventIdSpeedSkatingBigOverall ||
					sek.getSportEventId() == SportEvent.sportEventIdSpeedSkatingSmallOverall ||
					sek.getSportEventId() == SportEvent.sportEventIdSpeedSkatingSprintOverall)) {
				CompSeasonEventPartKey compSeasonEventPartKey = new CompSeasonEventPartKey(compSeasonEventKey,
						compSeasonEventPartId);

				List<PersonSport> ranking = new DbCalculation(stat).getTotalRanking(compSeasonEventPartKey);

				rankingPersonFragments.addAll(ranking.stream().map(x ->
						new SpeedSkatingRankingPersonFragment(competitionId, seasonId, compSeasonEventId,
								compSeasonEventPartId, x, clientId)).toList());

				DataFragmentUtil.fillDataFragments(rankingPersonFragments, getCacheKey());
			}
		}
	}

	@Override
	public boolean isValidOutput() {
		return !rankingPersonFragments.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("ranking", "personSport",
				rankingPersonFragments);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("ranking", rankingPersonFragments) + "}";
	}
}
