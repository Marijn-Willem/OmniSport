package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.time.LocalDateTime;

public class CompSeason extends SuperKeyEntity {
    private Integer triesAbsBonus;
    private Integer triesRelBonus;
    private Integer lossDiffBonus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int competitionId;
    private int seasonId;
    private String seasonName;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
            QueryUtil.convertIntegerToDbValue(triesAbsBonus),
            QueryUtil.convertIntegerToDbValue(triesRelBonus),
            QueryUtil.convertIntegerToDbValue(lossDiffBonus),
            QueryUtil.convertDateTimeToDbString(startDate),
            QueryUtil.convertDateTimeToDbString(endDate)
        };
    }

    public Integer getTriesAbsBonus() {
        return triesAbsBonus;
    }

    public void setTriesAbsBonus(Integer triesAbsBonus) {
        this.triesAbsBonus = triesAbsBonus;
    }

    public Integer getTriesRelBonus() {
        return triesRelBonus;
    }

    public void setTriesRelBonus(Integer triesRelBonus) {
        this.triesRelBonus = triesRelBonus;
    }

    public Integer getLossDiffBonus() {
        return lossDiffBonus;
    }

    public void setLossDiffBonus(Integer lossDiffBonus) {
        this.lossDiffBonus = lossDiffBonus;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }
}
