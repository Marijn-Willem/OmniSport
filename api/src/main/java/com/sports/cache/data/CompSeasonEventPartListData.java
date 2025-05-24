package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonEventPartListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.comparator.OrderableOrder;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonEventPartManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompSeasonEventPartListData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final Integer clientId;

	private final List<CompSeasonEventPartFragment> fragmentList = new ArrayList<>();

	public CompSeasonEventPartListData(int competitionId, int seasonId, int compSeasonEventId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new CompSeasonEventPartListKey(competitionId, seasonId, compSeasonEventId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonEventKey compSeasonEventKey = new CompSeasonEventKey(
				new CompSeasonKey(competitionId, seasonId), compSeasonEventId);

		List<CompSeasonEventPart> compSeasonEventParts = new CompSeasonEventPartManager(stat)
				.getCompSeasonEventPartsFromEvents(Collections.singletonList(compSeasonEventKey));

		compSeasonEventParts.sort(new OrderableOrder());

		fragmentList.addAll(compSeasonEventParts.stream().map(x ->
				new CompSeasonEventPartFragment(competitionId, seasonId, compSeasonEventId,
						x.getCompSeasonEventPartId(), clientId, DataFragmentUtil.getLevelForNestedList(nestingLevel))).toList());

		DataFragmentUtil.fillDataFragments(fragmentList, getCacheKey());
	}

	@Override
	public boolean isValidOutput() {
		return !fragmentList.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("compSeasonEventPartList",
				"compSeasonEventPart", fragmentList);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("compSeasonEventPartList", fragmentList) + "}";
	}

	@Override
	public String toYaml() {
		return new YamlUtil(nestingLevel).getArray("compSeasonEventPartList", fragmentList);
	}
}
