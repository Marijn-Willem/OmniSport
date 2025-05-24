package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonDivisionListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.CompDivision;
import com.sports.entity.key.CompSeasonKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompSeasonDivisionListData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final Integer clientId;

	private final List<CompSeasonDivisionFragment> compDivisionFragments = new ArrayList<>();

	public CompSeasonDivisionListData(int competitionId, int seasonId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new CompSeasonDivisionListKey(competitionId, seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
		List<CompDivision> sortedCompDivisions = new DbCalculation(stat).getSortedCompDivisions(compSeasonKey);

		compDivisionFragments.addAll(sortedCompDivisions.stream()
				.map(x -> new CompSeasonDivisionFragment(competitionId, seasonId, x.getCompDivisionId(), clientId,
						DataFragmentUtil.getLevelForNestedList(nestingLevel))).toList());

		DataFragmentUtil.fillDataFragments(compDivisionFragments, getCacheKey());
	}

	@Override
	public boolean isValidOutput() {
		return !compDivisionFragments.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("compDivisionList", "compDivision",
				compDivisionFragments);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("compDivisionList", compDivisionFragments) + "}";
	}

	@Override
	public String toYaml() {
		return new YamlUtil(nestingLevel).getArray("compDivisionList", compDivisionFragments);
	}
}
