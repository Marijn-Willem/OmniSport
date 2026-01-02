package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.KnockoutRankingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KnockoutRankingData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonPhaseId;
	private final Integer clientId;

	private final List<CompSeasonParticipantWithRankFragment> participantFragments = new ArrayList<>();

	public KnockoutRankingData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonPhaseId = compSeasonPhaseId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheDataKey() {
		return new KnockoutRankingKey(competitionId, seasonId, compSeasonPhaseId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonPhaseKey cspKey = new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId), compSeasonPhaseId);
		CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
				? extends CompSeasonPhaseParticipantKey,
				? extends Participant,
				? extends SuperKeyEntity,
				? extends H2HMatchKey,
				? extends H2HMatch,
				? extends H2HMatchPartKey,
				? extends H2HMatchPart,
				? extends H2HMatchPartStatKey,
				? extends H2HMatchPartStat> factory =
				new com.sports.logic.calculation.DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);

		participantFragments.addAll(new DbCalculation(stat).getKnockoutPhaseRanking(cspKey, factory).stream().map(x ->
				new CompSeasonParticipantWithRankFragment(competitionId, seasonId, x, clientId,
						YamlUtil.getLevelForNestedList(nestingLevel))).toList());

		DataFragmentUtil.fillDataFragments(participantFragments, getCacheDataKey());
	}

	@Override
	public boolean isValidOutput() {
		return !participantFragments.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("ranking", "participant", participantFragments);
	}

	@Override
	public String toJson() {
		return "{" +  JsonUtil.getArray("ranking", participantFragments) + "}";
	}

	@Override
	public String toYaml() {
		return new YamlUtil(nestingLevel).getArray("ranking", participantFragments);
	}
}
