package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportDisciplineKey;

import java.time.LocalDateTime;

public class CompSeasonEventPart extends SuperKeyEntity implements Orderable, DescribedEntity {
    private int sportId;
    private int sportDisciplineId;
    private Integer eventPartNameId;
    private int order;
    private Integer stage;
    private LocalDateTime date;
    private String externalSource;
    private boolean isFinal;

    private int competitionId;
    private int seasonId;
    private int compSeasonEventId;
    private int compSeasonEventPartId;
    private String description;
    private LocalDateTime compSeasonEndDate;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                String.valueOf(sportId),
                String.valueOf(sportDisciplineId),
                QueryUtil.convertIntegerToDbValue(eventPartNameId),
                String.valueOf(order),
                QueryUtil.convertIntegerToDbValue(stage),
                QueryUtil.convertDateTimeToDbString(date),
                QueryUtil.convertStringToDbValue(externalSource),
                QueryUtil.convertBooleanToDbValue(isFinal)
        };
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getSportDisciplineId() {
        return sportDisciplineId;
    }

    public void setSportDisciplineId(int sportDisciplineId) {
        this.sportDisciplineId = sportDisciplineId;
    }

    public Integer getEventPartNameId() {
        return eventPartNameId;
    }

    public void setEventPartNameId(Integer eventPartNameId) {
        this.eventPartNameId = eventPartNameId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public void setCompSeasonEventId(int compSeasonEventId) {
        this.compSeasonEventId = compSeasonEventId;
    }

    public int getCompSeasonEventPartId() {
        return compSeasonEventPartId;
    }

    public void setCompSeasonEventPartId(int compSeasonEventPartId) {
        this.compSeasonEventPartId = compSeasonEventPartId;
    }

    public CompSeasonEventPartKey getCompSeasonEventPartKey() {
        return new CompSeasonEventPartKey(
                new CompSeasonEventKey(
                        new CompSeasonKey(competitionId, seasonId), compSeasonEventId),
                compSeasonEventPartId
        );
    }

    public SportDisciplineKey getSportDisciplineKey() {
        return new SportDisciplineKey(sportId, sportDisciplineId);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCompSeasonEndDate() {
        return compSeasonEndDate;
    }

    public void setCompSeasonEndDate(LocalDateTime compSeasonEndDate) {
        this.compSeasonEndDate = compSeasonEndDate;
    }
}
