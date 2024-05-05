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
    private final int competitionId;
    private final int seasonId;
    private final int teamId;
    private final int clientId;

    private String description;

    public CompSeasonTeamFragment(int competitionId, int seasonId, int teamId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.teamId = teamId;
        this.clientId = clientId;
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", teamId) +
                XmlUtil.getTag("description", description);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", teamId) + "," +
                JsonUtil.getEntry("description", description);
    }

    @Override
    public CacheKey getCacheKey() {
        return new CompSeasonTeamKey(competitionId, seasonId, teamId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Team team = new TeamManager(stat).getEntityFromId(teamId);
        DescribedEntityUtil describedEntityUtil = new DescribedEntityUtil(clientId, getCacheDataKey(), stat);

        description = describedEntityUtil.getTeamString(team, new CompSeasonKey(competitionId, seasonId));
    }
}
