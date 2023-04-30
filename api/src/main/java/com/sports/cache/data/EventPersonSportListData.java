package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EventPersonSportListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.PersonSport;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.EventPersonSportManager;
import com.sports.entity.manager.PersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventPersonSportListData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final int compSeasonEventId;
	private final Integer clientId;

	private final List<EventPersonSportFragment> eventPersonSportFragments = new ArrayList<>();

	public EventPersonSportListData(int competitionId, int seasonId, int compSeasonEventId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.compSeasonEventId = compSeasonEventId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new EventPersonSportListKey(competitionId, seasonId, compSeasonEventId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonEventKey cseKey = new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId),
				compSeasonEventId);

		List<Integer> personSportIds = new EventPersonSportManager(stat).getPersonSportIdsCompSeasonEvent(cseKey);
		List<PersonSport> personSports = new PersonSportManager(stat).getParticipantList(personSportIds);
		personSports.sort(new DescribedEntityDescription());

		eventPersonSportFragments.addAll(personSports.stream().map(x ->
				new EventPersonSportFragment(competitionId, seasonId, compSeasonEventId, x.getId(), clientId)).toList());

		DataFragmentUtil.fillDataFragments(eventPersonSportFragments, getCacheKey());
	}

	@Override
	public boolean isValidOutput() {
		return !eventPersonSportFragments.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("eventPersonSportList", "eventPersonSport",
				eventPersonSportFragments);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("eventPersonSportList",
				eventPersonSportFragments) + "}";
	}
}
