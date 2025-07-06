package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CyclingRoadStageWinnersKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonEventPartStage;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.entity.manager.EventPartTeamManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CyclingRoadStageWinnersData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final Integer clientId;

	private final List<StageWinnerFragment> stageWinnerFragments = new ArrayList<>();

	public CyclingRoadStageWinnersData(int competitionId, int seasonId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheDataKey() {
		return new CyclingRoadStageWinnersKey(competitionId, seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonKey csKey = new CompSeasonKey(competitionId, seasonId);

		SportEventKey sekStage = new SportEventKey(Sport.sportIdCyclingRoad, SportEvent.sportEventIdCyclingRoadStage);
		SportEventKey sekStageTeam = new SportEventKey(Sport.sportIdCyclingRoad, SportEvent.sportEventIdCyclingRoadStageTeam);

		List<SportEventKey> sportEventKeys = new ArrayList<>() {{
			add(sekStage);
			add(sekStageTeam);
		}};

		List<CompSeasonEvent> compSeasonEvents = new CompSeasonEventManager(stat)
				.getCompSeasonEventsBySportEvents(csKey, sportEventKeys);

		if (!compSeasonEvents.isEmpty()) {
			List<CompSeasonEventKey> compSeasonEventKeys = compSeasonEvents.stream()
					.map(x -> new CompSeasonEventKey(csKey, x.getCompSeasonEventId())).toList();

			List<CompSeasonEventPart> compSeasonEventParts = new CompSeasonEventPartManager(stat)
					.getCompSeasonEventPartsFromEvents(compSeasonEventKeys);
			compSeasonEventParts.sort(new CompSeasonEventPartStage());

			CompSeasonEvent cseStage = getCompSeasonEventForSportsEvent(compSeasonEvents, sekStage);
			CompSeasonEvent cseStageTeam = getCompSeasonEventForSportsEvent(compSeasonEvents, sekStageTeam);

			CompSeasonEventKey cseKeyStage = cseStage != null ?
					new CompSeasonEventKey(csKey, cseStage.getCompSeasonEventId()) : null;
			CompSeasonEventKey cseKeyStageTeam = cseStageTeam != null ?
					new CompSeasonEventKey(csKey, cseStageTeam.getCompSeasonEventId()) : null;

			Map<CompSeasonEventPartKey, EventPartPersonSport> eventPartPersonSportMap = new HashMap<>() {{
				if (cseKeyStage != null) {
					new EventPartPersonSportManager(stat).getPartParticipantsWithRankOne(cseKeyStage).forEach(x ->
							put(new CompSeasonEventPartKey(cseKeyStage, x.getCompSeasonEventPartId()), x)
					);
				}
			}};

			Map<CompSeasonEventPartKey, EventPartTeam> eventPartTeamMap = new HashMap<>() {{
				if (cseKeyStageTeam != null) {
					new EventPartTeamManager(stat).getPartParticipantsWithRankOne(cseKeyStageTeam).forEach(x ->
							put(new CompSeasonEventPartKey(cseKeyStageTeam, x.getCompSeasonEventPartId()), x)
					);
				}
			}};

			compSeasonEventParts.forEach(x -> {
				addFragment(cseKeyStage, x, eventPartPersonSportMap);
				addFragment(cseKeyStageTeam, x, eventPartTeamMap);
			});

			DataFragmentUtil.fillDataFragments(stageWinnerFragments, getCacheDataKey());
		}
	}

	@Override
	public boolean isValidOutput() {
		return !stageWinnerFragments.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("stageWinnerList", "stageWinner", stageWinnerFragments);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("stageWinnerList", stageWinnerFragments) + "}";
	}

	@Override
	public String toYaml() {
		return new YamlUtil(nestingLevel).getArray("stageWinnerList", stageWinnerFragments);
	}

	private CompSeasonEvent getCompSeasonEventForSportsEvent(List<CompSeasonEvent> compSeasonEvents, SportEventKey seKey) {
		for (CompSeasonEvent compSeasonEvent : compSeasonEvents)
			if (seKey.equals(compSeasonEvent.getSportEventKey()))
				return compSeasonEvent;

		return null;
	}

	private <T extends AlcifoPartParticipant> void addFragment(CompSeasonEventKey cseKey,
															   CompSeasonEventPart compSeasonEventPart,
															   Map<CompSeasonEventPartKey, T> partParticipantMap) {
		if (cseKey != null &&
				compSeasonEventPart.getStage() != null &&
				compSeasonEventPart.getCompSeasonEventPartKey().getSuperKey().equals(cseKey)) {
			CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(cseKey, compSeasonEventPart.getCompSeasonEventPartId());
			T partParticipant = partParticipantMap.get(csepKey);

			if (partParticipant != null) {
				int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevel);

				stageWinnerFragments.add(new StageWinnerFragment(competitionId, seasonId,
						cseKey.getCompSeasonEventId(), partParticipant.getParticipantId(),
						compSeasonEventPart.getStage(), clientId, nestingLevelList));
			}
		}
	}
}
