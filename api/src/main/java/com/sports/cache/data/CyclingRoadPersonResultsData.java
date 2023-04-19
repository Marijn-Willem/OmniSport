package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CyclingRoadPersonResultsKey;
import com.sports.cache.util.ClientCyclingRoadPersonResultFilter;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.cyclingroad.DbCalculation;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportEventKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CyclingRoadPersonResultsData extends OutputData {
	private final int personSportId;
	private final int seasonId;
	private final Integer clientId;

	private final List<CyclingRoadPersonResultFragment> cyclingRoadPersonResultFragmentList = new ArrayList<>();

	public CyclingRoadPersonResultsData(int personSportId, int seasonId, Integer clientId) {
		this.personSportId = personSportId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new CyclingRoadPersonResultsKey(personSportId, seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		List<EventPartPersonSport> eventPartPersonSports = new DbCalculation(stat).getPersonResultsInSeason(personSportId, seasonId);
		ClientCyclingRoadPersonResultFilter filter = new ClientCyclingRoadPersonResultFilter(clientId, getCacheKey(), stat);

		cyclingRoadPersonResultFragmentList.addAll(
			eventPartPersonSports.stream().map(x -> {
					CompSeasonEventPartKey csepKey = x.getCompSeasonEventPart().getCompSeasonEventPartKey();
					CompSeasonKey compSeasonKey = csepKey.getSuperKey().getSuperKey();
					SportEventKey sportEventKey = csepKey.getSuperKey().getSportEventKey();

					return new CyclingRoadPersonResultFragment(
							compSeasonKey.getCompetitionId(), compSeasonKey.getSeasonId(), sportEventKey.getSportEventId(),
							csepKey.getCompSeasonEventPartId(), personSportId, x.getRank(), x.getNoCountResultId(),
							x.getEventDate(), clientId);
				})
				.filter(filter::isElementAllowed)
				.toList()
		);

		DataFragmentUtil.fillDataFragments(cyclingRoadPersonResultFragmentList, getCacheKey());
	}

	@Override
	public boolean isValidOutput() {
		return !cyclingRoadPersonResultFragmentList.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("personResults", "personResult",
				cyclingRoadPersonResultFragmentList);
	}

	@Override
	public String toJson() {
		return "{" +
				JsonUtil.getArray("personResults", cyclingRoadPersonResultFragmentList) +
				"}";
	}
}
