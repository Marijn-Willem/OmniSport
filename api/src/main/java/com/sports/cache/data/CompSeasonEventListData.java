package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonEventListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.calc.alcifo.Calculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.SportEvent;
import com.sports.entity.comparator.CompSeasonEventNameGenderId;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SportEventManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompSeasonEventListData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final Integer clientId;

	private final List<CompSeasonEventFragment> compSeasonEventFragments = new ArrayList<>();

	public CompSeasonEventListData(int competitionId, int seasonId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new CompSeasonEventListKey(competitionId, seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
		List<CompSeasonEvent> compSeasonEvents = new CompSeasonEventManager(stat).getCompSeasonEvents(compSeasonKey);
		List<SportEvent> sportEvents = new SportEventManager(stat).getSportEventListByKeys(compSeasonEvents.stream().map(
				CompSeasonEvent::getSportEventKey).toList());

		Calculation.setSportEventNames(compSeasonEvents, sportEvents);
		compSeasonEvents.sort(new CompSeasonEventNameGenderId());

		compSeasonEventFragments.addAll(compSeasonEvents.stream().map(x -> new CompSeasonEventFragment(
				compSeasonKey, x, clientId, DataFragmentUtil.getLevelForNestedList(nestingLevel))).toList());

		DataFragmentUtil.fillDataFragments(compSeasonEventFragments, getCacheKey());
	}

	@Override
	public boolean isValidOutput() {
		return !compSeasonEventFragments.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("compSeasonEventList", "compSeasonEvent",
				compSeasonEventFragments);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("compSeasonEventList", compSeasonEventFragments) + "}";
	}

	@Override
	public String toYaml() {
		return new YamlUtil(nestingLevel).getArray("compSeasonEventList", compSeasonEventFragments);
	}
}
