package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompSeasonTeamKey;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.TeamManager;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonTeamFragment extends WritableFragment {
    final int competitionId;
    final int seasonId;
    final int teamId;
    final int clientId;

    private String description;
    private int elo;

    public CompSeasonTeamFragment(int competitionId, int seasonId, int teamId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.teamId = teamId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new CompSeasonTeamKey(competitionId, seasonId, teamId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Team team = new TeamManager(stat).getEntityFromId(teamId);
        DescribedEntityUtil describedEntityUtil = new DescribedEntityUtil(clientId, getCacheDataKey(), stat);

        elo = team.getElo();

        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        description = describedEntityUtil.getTeamString(team, compSeasonKey);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", teamId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getTag("elo", elo);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", teamId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getEntry("elo", elo);
    }
}
