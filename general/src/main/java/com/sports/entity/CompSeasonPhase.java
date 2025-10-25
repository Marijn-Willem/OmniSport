package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.CompSeasonPhaseKey;

import java.time.LocalDateTime;

public class CompSeasonPhase extends SuperKeyEntity {
    private Integer parentPhaseId;
    private Integer round;
    private boolean knockoutParent;
    private Integer bestOf1;
    private Integer bestOf2;
    private Integer bestOfDec;
    private boolean finished;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean hasStanding;
    private Integer parentOrder;
    private Integer expandFactor;
    private boolean hasDivisionStandings;
    private int phaseTypeId;
    private boolean hasParentMatches;

    private CompSeasonPhaseKey compSeasonPhaseKey;
    private String description;
    private boolean canBeDeleted;
    private boolean hasKnockoutParent;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(parentPhaseId),
                QueryUtil.convertIntegerToDbValue(round),
                QueryUtil.convertBooleanToDbValue(knockoutParent),
                QueryUtil.convertIntegerToDbValue(bestOf1),
                QueryUtil.convertIntegerToDbValue(bestOf2),
                QueryUtil.convertIntegerToDbValue(bestOfDec),
                QueryUtil.convertBooleanToDbValue(finished),
                QueryUtil.convertDateTimeToDbString(startDate),
                QueryUtil.convertDateTimeToDbString(endDate),
                QueryUtil.convertBooleanToDbValue(hasStanding),
                QueryUtil.convertIntegerToDbValue(parentOrder),
                QueryUtil.convertIntegerToDbValue(expandFactor),
                QueryUtil.convertBooleanToDbValue(hasDivisionStandings),
                "" + phaseTypeId,
                QueryUtil.convertBooleanToDbValue(hasParentMatches)
            };
    }

    public CompSeasonPhaseKey getParentPhaseKey() {
        if (parentPhaseId != null)
            return new CompSeasonPhaseKey(compSeasonPhaseKey.getSuperKey(), parentPhaseId);

        return null;
    }

    public Integer getParentPhaseId() {
        return parentPhaseId;
    }

    public void setParentPhaseId(Integer parentPhaseId) {
        this.parentPhaseId = parentPhaseId;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public boolean isKnockoutParent() {
        return knockoutParent;
    }

    public void setKnockoutParent(boolean knockoutParent) {
        this.knockoutParent = knockoutParent;
    }

    public Integer getBestOf1() {
        return bestOf1;
    }

    public void setBestOf1(Integer bestOf1) {
        this.bestOf1 = bestOf1;
    }

    public Integer getBestOf2() {
        return bestOf2;
    }

    public void setBestOf2(Integer bestOf2) {
        this.bestOf2 = bestOf2;
    }

    public Integer getBestOfDec() {
        return bestOfDec;
    }

    public void setBestOfDec(Integer bestOfDec) {
        this.bestOfDec = bestOfDec;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
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

    public boolean isHasStanding() {
        return hasStanding;
    }

    public void setHasStanding(boolean hasStanding) {
        this.hasStanding = hasStanding;
    }

    public Integer getParentOrder() {
        return parentOrder;
    }

    public void setParentOrder(Integer parentOrder) {
        this.parentOrder = parentOrder;
    }

    public Integer getExpandFactor() {
        return expandFactor;
    }

    public void setExpandFactor(Integer expandFactor) {
        this.expandFactor = expandFactor;
    }

    public boolean isHasDivisionStandings() {
        return hasDivisionStandings;
    }

    public void setHasDivisionStandings(boolean hasDivisionStandings) {
        this.hasDivisionStandings = hasDivisionStandings;
    }

    public int getPhaseTypeId() {
        return phaseTypeId;
    }

    public void setPhaseTypeId(int phaseTypeId) {
        this.phaseTypeId = phaseTypeId;
    }

    public boolean isHasParentMatches() {
        return hasParentMatches;
    }

    public void setHasParentMatches(boolean hasParentMatches) {
        this.hasParentMatches = hasParentMatches;
    }

    public CompSeasonPhaseKey getCompSeasonPhaseKey() {
        return compSeasonPhaseKey;
    }

    public void setCompSeasonPhaseKey(CompSeasonPhaseKey compSeasonPhaseKey) {
        this.compSeasonPhaseKey = compSeasonPhaseKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    public boolean isHasKnockoutParent() {
        return hasKnockoutParent;
    }

    public void setHasKnockoutParent(boolean hasKnockoutParent) {
        this.hasKnockoutParent = hasKnockoutParent;
    }
}
