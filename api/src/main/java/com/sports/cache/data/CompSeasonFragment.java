package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.CompSeason;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SeasonManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CompSeasonFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;

    private LocalDateTime startDate;
    private String competitionName;
    private String seasonName;

    public CompSeasonFragment(CompSeasonKey compSeasonKey) {
        this.competitionId = compSeasonKey.getCompetitionId();
        this.seasonId = compSeasonKey.getSeasonId();
    }

    @Override
    public int hashCode() {
        return getCompSeasonKey().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonFragment &&
                getCompSeasonKey().equals(((CompSeasonFragment) obj).getCompSeasonKey());
    }

    @Override
    public CacheKey getCacheKey() {
        return new com.sports.cache.key.CompSeasonKey(competitionId, seasonId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeason compSeason = new CompSeasonManager(stat).getCompSeason(getCompSeasonKey());
        startDate = compSeason.getStartDate();

        competitionName = new CompetitionManager(stat).getCompetition(competitionId).getName();
        seasonName = new SeasonManager(stat).getSeason(seasonId).getName();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("competitionName", competitionName) +
                XmlUtil.getTag("seasonName", seasonName) +
                XmlUtil.getTag("startDate", startDate);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("competitionName", competitionName) + "," +
                JsonUtil.getEntry("seasonName", seasonName) + "," +
                JsonUtil.getEntry("startDate", startDate);
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public CompSeasonKey getCompSeasonKey() {
        return new CompSeasonKey(competitionId, seasonId);
    }
}
