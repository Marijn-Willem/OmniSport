package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.TeamInCrocoCupKey;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.Team;

import java.sql.SQLException;
import java.sql.Statement;

public class TeamInCrocoCupFragment extends WritableFragment {
    private final int teamId;
    private final int clientId;

    private String description;
    private final Integer clubId;
    private final Integer nocId;
    private final int elo;
    private final Integer points;
    private final boolean showMatches;

    public TeamInCrocoCupFragment(Team team, int clientId, boolean showMatches, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);

        this.teamId = team.getId();
        this.clientId = clientId;
        this.description = team.getDescription();
        this.clubId = team.getClubId();
        this.nocId = team.getNocId();
        this.elo = team.getElo();
        this.points = team.getPoints();
        this.showMatches = showMatches;
    }

    @Override
    public CacheKey getCacheKey() {
        return new TeamInCrocoCupKey(teamId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Team team = new Team();
        team.setId(teamId);
        team.setDescription(description);
        team.setClubId(clubId);
        team.setNocId(nocId);

        description = new DescribedEntityUtil(clientId, getCacheDataKey(), stat).getTeamString(team);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", teamId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getTag("elo", elo) +
                (showMatches ? XmlUtil.getTag("matches", points) : "");
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", teamId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getEntry("elo", elo) +
                (showMatches ? "," + JsonUtil.getEntry("matches", points) : "");
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", teamId, isInList) +
                yamlUtil.getEntry("description", description) +
                yamlUtil.getEntry("elo", elo) +
                (showMatches ? yamlUtil.getEntry("matches", points) : "");
    }
}
