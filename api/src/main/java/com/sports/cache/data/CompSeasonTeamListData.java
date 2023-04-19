package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonTeamListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.TeamManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompSeasonTeamListData extends OutputData {
	private final int competitionId;
	private final int seasonId;
	private final Integer clientId;

	private final List<CompSeasonTeamWithPersonSportsFragment> teamFragments = new ArrayList<>();

	public CompSeasonTeamListData(int competitionId, int seasonId, Integer clientId) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.clientId = clientId;
	}

	@Override
	public CacheDataKey getCacheKey() {
		return new CompSeasonTeamListKey(competitionId, seasonId, clientId);
	}

	@Override
	public void fill(Statement stat) throws SQLException {
		List<Integer> teamIds = new CompSeasonTeamManager(stat).getParticipantIdsCompSeason(
				new CompSeasonKey(competitionId, seasonId));
		List<Team> teamList = new TeamManager(stat).getTeamList(teamIds);
		teamList.sort(new DescribedEntityDescription());

		teamFragments.addAll(teamList.stream().map(x ->
				new CompSeasonTeamWithPersonSportsFragment(competitionId, seasonId, x.getId(), clientId)).toList());

		DataFragmentUtil.fillDataFragments(teamFragments, getCacheKey());
	}

	@Override
	public boolean isValidOutput() {
		return true;
	}

	@Override
	public String toXML() {
		return XmlUtil.getTopLevelXmlList("teamList", "team", teamFragments);
	}

	@Override
	public String toJson() {
		return "{" + JsonUtil.getArray("teamList", teamFragments) + "}";
	}
}
