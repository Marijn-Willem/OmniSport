package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompSeasonDivisionKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.TeamManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompSeasonDivisionFragment extends CompDivisionFragment {
    private final int seasonId;
    private final int clientId;

    private final List<CompSeasonParticipantFragment> teamFragments = new ArrayList<>();

    public CompSeasonDivisionFragment(int competitionId, int seasonId, int compDivisionId, int clientId, int nestingLevel) {
        super(competitionId, compDivisionId, nestingLevel, true);
        this.seasonId = seasonId;
        this.clientId = clientId;
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);

        com.sports.entity.key.CompSeasonDivisionKey csdKey = new com.sports.entity.key.CompSeasonDivisionKey(
                new CompSeasonKey(competitionId, seasonId), compDivisionId);

        List<CompSeasonTeamKey> compSeasonTeamKeys = new CompSeasonTeamManager(stat).getTeamsInCompSeasonDivision(
                Collections.singletonList(csdKey));

        List<Team> teams = new TeamManager(stat).getTeamList(
                compSeasonTeamKeys.stream().map(CompSeasonTeamKey::getSpecificId).toList());

        teams.sort(new DescribedEntityDescription());

        teamFragments.addAll(teams.stream().map(x ->
                new CompSeasonParticipantFragment(competitionId, seasonId, x.getId(), clientId,
                        DataFragmentUtil.getLevelForNestedList(nestingLevel), true)).toList());

        DataFragmentUtil.fillDataFragments(teamFragments, getCacheDataKey());
    }

    @Override
    public CacheKey getCacheKey() {
        return new CompSeasonDivisionKey(competitionId, seasonId, compDivisionId);
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getEnclosedXmlList("teamList", "team", teamFragments);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getArray("teamList", teamFragments);
    }

    @Override
    public String toYaml() {
        return super.toYaml() + new YamlUtil(nestingLevel).getArray("teamList", teamFragments);
    }
}
