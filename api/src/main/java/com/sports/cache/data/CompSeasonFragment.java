package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.entity.CompSeason;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CompSeasonFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;

    private LocalDateTime startDate;

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
    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public String toJson() {
        return null;
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
