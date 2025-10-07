package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EventPersonSportRankingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.EventPersonSport;
import com.sports.entity.comparator.EventPersonSportRankDescription;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventPersonSportRankingData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final Integer clientId;

    private final List<EventPersonSportForRankingFragment> fragmentList = new ArrayList<>();

	public EventPersonSportRankingData(int competitionId, int seasonId, int compSeasonEventId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheDataKey() {
		return new EventPersonSportRankingKey(competitionId, seasonId, compSeasonEventId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
        int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevel);

        CompSeasonEventKey cseKey = new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId), compSeasonEventId);

        List<EventPersonSport> eventPersonSports = new DbCalculation(stat).getEventPersonSportsWithDescription(cseKey);
        eventPersonSports.sort(new EventPersonSportRankDescription());

        eventPersonSports.forEach(x -> fragmentList.add(new EventPersonSportForRankingFragment(
                competitionId, seasonId, compSeasonEventId, x.getSpecificId(), clientId, nestingLevelList)));

        DataFragmentUtil.fillDataFragments(fragmentList, getCacheDataKey());
	}

	@Override
	public boolean isValidOutput() {
		return !fragmentList.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("eventPersonSportList", "eventPersonSport", fragmentList);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("eventPersonSportList", fragmentList) + "}";
	}

	@Override
	public String toYaml() {
		return new YamlUtil(nestingLevel).getArray("eventPersonSportList", fragmentList);
	}
}
