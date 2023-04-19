package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportEventKey;

import java.time.LocalDateTime;

public class CompSeasonEventPart extends SuperKeyEntity implements Orderable, DescribedEntity {
    private Integer sportEventPartId;
    private Integer sportDisciplineId;
    private Integer eventPartNameId;
    private int order;
    private Integer stage;
    private LocalDateTime date;
    private String externalSource;

    private int competitionId;
    private int seasonId;
    private int sportId;
    private int sportEventId;
    private int compSeasonEventPartId;
    private String description;
    private LocalDateTime compSeasonEndDate;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(sportEventPartId),
                QueryUtil.convertIntegerToDbValue(sportDisciplineId),
                QueryUtil.convertIntegerToDbValue(eventPartNameId),
                "" + order,
                QueryUtil.convertIntegerToDbValue(stage),
                QueryUtil.convertDateTimeToDbString(date),
                QueryUtil.convertStringToDbValue(externalSource)
        };
    }

    public Integer getSportEventPartId() {
        return sportEventPartId;
    }

    public void setSportEventPartId(Integer sportEventPartId) {
        this.sportEventPartId = sportEventPartId;
    }

    public Integer getSportDisciplineId() {
        return sportDisciplineId;
    }

    public void setSportDisciplineId(Integer sportDisciplineId) {
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

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getSportEventId() {
        return sportEventId;
    }

    public void setSportEventId(int sportEventId) {
        this.sportEventId = sportEventId;
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
                        new CompSeasonKey(competitionId, seasonId), sportId, sportEventId),
                compSeasonEventPartId
        );
    }

    public SportEventKey getSportEventKey() {
        return new SportEventKey(sportId, sportEventId);
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
