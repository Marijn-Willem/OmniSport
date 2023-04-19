package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EventPersonSportListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Competition;
import com.sports.entity.PersonSport;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.EventPersonSportManager;
import com.sports.entity.manager.PersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventPersonSportListData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final int sportEventId;
	private final Integer clientId;

	private final List<EventPersonSportFragment> eventPersonSportFragments = new ArrayList<>();

	public EventPersonSportListData(int competitionId, int seasonId, int sportEventId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.sportEventId = sportEventId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new EventPersonSportListKey(competitionId, seasonId, sportEventId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

		if (competition != null) {
			CompSeasonEventKey cseKey = new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId),
					competition.getSportId(), sportEventId);

			List<Integer> personSportIds = new EventPersonSportManager(stat).getPersonSportIdsCompSeasonEvent(cseKey);
			List<PersonSport> personSports = new PersonSportManager(stat).getParticipantList(personSportIds);
			personSports.sort(new DescribedEntityDescription());

			eventPersonSportFragments.addAll(personSports.stream().map(x ->
					new EventPersonSportFragment(competitionId, seasonId, sportEventId, x.getId(), clientId)).toList());

			DataFragmentUtil.fillDataFragments(eventPersonSportFragments, getCacheKey());
		}
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
