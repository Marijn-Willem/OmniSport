package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.MatchListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.comparator.MatchDate;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchListData extends OutputData {
	final int competitionId;
	final int seasonId;
	final Integer clientId;

	private final List<MatchListPhaseFragment> matchListPhaseFragmentList = new ArrayList<>();

	<MK extends H2HMatchKey, M extends H2HMatch> List<M> getH2HMatches(Statement stat, CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
			? extends CompSeasonPhaseParticipantKey,
			? extends Participant,
			? extends SuperKeyEntity,
			? extends SuperKeyEntity,
			MK,
			M,
			? extends H2HMatchPartKey,
			? extends H2HMatchPart,
			? extends H2HMatchPartStatKey,
			? extends H2HMatchPartStat> factory, CompSeasonKey compSeasonKey) throws SQLException {
		H2HMatchManager<MK, M> manager = factory.getH2HObjectFactory().getManager(stat);
		return manager.getH2HMatchList(compSeasonKey, null);
	}

	MatchListPhaseFragment getMatchListPhaseFragment(int competitionId, int seasonId, int compSeasonPhaseId,
													 List<H2HMatch> h2HMatches) {
		return new MatchListPhaseFragment(competitionId, seasonId, compSeasonPhaseId, h2HMatches, clientId);
	}

	public MatchListData(int competitionId, int seasonId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new MatchListKey(competitionId, seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

		CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
				? extends CompSeasonPhaseParticipantKey,
				? extends Participant,
				? extends SuperKeyEntity,
				? extends SuperKeyEntity,
				? extends H2HMatchKey,
				? extends H2HMatch,
				? extends H2HMatchPartKey,
				? extends H2HMatchPart,
				? extends H2HMatchPartStatKey,
				? extends H2HMatchPartStat> factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);
		List<? extends H2HMatch> matchList = getH2HMatches(stat, factory, compSeasonKey);
		matchList.sort(new MatchDate());

		Map<Integer, List<H2HMatch>> matchesByCompSeasonPhase = new HashMap<>() {{
			matchList.forEach(x -> {
				int compSeasonPhaseId = x.getCompSeasonPhaseId();
				if (!containsKey(compSeasonPhaseId))
					put(compSeasonPhaseId, new ArrayList<>());

				get(compSeasonPhaseId).add(x);
			});
		}};

		List<CompSeasonPhase> compSeasonPhases = new CompSeasonPhaseManager(stat).getCompSeasonPhases(compSeasonKey);
		compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());

		compSeasonPhases.forEach(x -> {
			int compSeasonPhaseId = x.getCompSeasonPhaseKey().getCompSeasonPhaseId();
			if (matchesByCompSeasonPhase.containsKey(compSeasonPhaseId))
				matchListPhaseFragmentList.add(getMatchListPhaseFragment(competitionId, seasonId,
						x.getCompSeasonPhaseKey().getCompSeasonPhaseId(),
						matchesByCompSeasonPhase.get(compSeasonPhaseId)));
		});

		DataFragmentUtil.fillDataFragments(matchListPhaseFragmentList, getCacheKey());
	}

	@Override
	public boolean isValidOutput() {
		return !matchListPhaseFragmentList.isEmpty();
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("compSeasonPhaseList", "compSeasonPhase",
				matchListPhaseFragmentList);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("compSeasonPhaseList", matchListPhaseFragmentList) + "}";
	}
}
