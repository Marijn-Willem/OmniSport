package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.logic.util.Util;

import java.time.LocalDateTime;

public abstract class H2HMatch extends SuperKeyEntity implements WithH2HMatchParticipantsDate {
    private boolean finished;
    private int compSeasonPhaseId;
    private Integer knockoutOrder;
    private LocalDateTime date;
    private Integer parentMatchId;

    private CompSeasonPhaseKey compSeasonPhaseKey;

    abstract void setSpecificId(int specificId);
    public abstract int getSpecificId();
    abstract String[] getSpecificPropertiesInSQLStrings();
    abstract void copySpecific(H2HMatch other);
    abstract void addScoresFromSpecific(H2HMatch matchFrom);
    public abstract void setParticipant1Id(Integer participant1id);
    public abstract Integer getParticipant1Id();
    public abstract void setParticipant2Id(Integer participant2id);
    public abstract Integer getParticipant2Id();
    public abstract boolean isParticipant1Start();
    public abstract void setParticipant1Start(boolean participant1Start);
    public abstract Integer getParticipant1NcrId();
    public abstract void setParticipant1NcrId(Integer participant1NcrId);
    public abstract Integer getParticipant2NcrId();
    public abstract void setParticipant2NcrId(Integer participant2NcrId);
    public abstract void setScore1_1(Integer score1_1);
    public abstract Integer getScore1_1();
    public abstract void setScore1_2(Integer score1_2);
    public abstract Integer getScore1_2();

    @Override
    public String[] getPropertiesInSQLStrings() {
        String[] generalProps = {
                QueryUtil.convertIntegerToDbValue(getParticipant1Id()),
                QueryUtil.convertIntegerToDbValue(getParticipant2Id()),
                QueryUtil.convertIntegerToDbValue(getScore1_1()),
                QueryUtil.convertIntegerToDbValue(getScore1_2()),
                QueryUtil.convertIntegerToDbValue(getParticipant1NcrId()),
                QueryUtil.convertIntegerToDbValue(getParticipant2NcrId()),
                QueryUtil.convertBooleanToDbValue(finished),
                "" + compSeasonPhaseId,
                QueryUtil.convertIntegerToDbValue(knockoutOrder),
                QueryUtil.convertDateTimeToDbString(date),
                QueryUtil.convertIntegerToDbValue(parentMatchId)
        };

        return Util.concatenateStringArrays(getSpecificPropertiesInSQLStrings(), generalProps);
    }

    public int getWinnerId() {
        return isParticipant1Win() ? getParticipant1Id() : getParticipant2Id();
    }

    public boolean isParticipant1Win() {
        return isFinished() && (
                getParticipant2NcrId() != null ||
                        (getScore1_1() != null && getScore1_2() != null && getScore1_1() > getScore1_2())
        );
    }

    public boolean isParticipant2Win() {
        return isFinished() && (
                getParticipant1NcrId() != null ||
                        (getScore1_1() != null && getScore1_2() != null && getScore1_2() > getScore1_1())
        );
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getCompSeasonPhaseId() {
        return compSeasonPhaseId;
    }

    public void setCompSeasonPhaseId(int compSeasonPhaseId) {
        this.compSeasonPhaseId = compSeasonPhaseId;
    }

    public Integer getKnockoutOrder() {
        return knockoutOrder;
    }

    public void setKnockoutOrder(Integer knockoutOrder) {
        this.knockoutOrder = knockoutOrder;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getParentMatchId() {
        return parentMatchId;
    }

    public void setParentMatchId(Integer parentMatchId) {
        this.parentMatchId = parentMatchId;
    }

    public CompSeasonPhaseKey getCompSeasonPhaseKey() {
        return compSeasonPhaseKey;
    }

    public void setCompSeasonPhaseKey(CompSeasonPhaseKey compSeasonPhaseKey) {
        this.compSeasonPhaseKey = compSeasonPhaseKey;
    }

    public void copy(H2HMatch other) {
        other.setSpecificId(getSpecificId());
        other.setParticipant1Id(getParticipant1Id());
        other.setParticipant2Id(getParticipant2Id());
        other.setScore1_1(getScore1_1());
        other.setScore1_2(getScore1_2());
        other.setParticipant1NcrId(getParticipant1NcrId());
        other.setParticipant2NcrId(getParticipant2NcrId());
        other.finished = finished;
        other.compSeasonPhaseId = compSeasonPhaseId;
        other.knockoutOrder = knockoutOrder;
        other.date = date;
        other.parentMatchId = parentMatchId;
        other.setCompSeasonPhaseKey(compSeasonPhaseKey);

        copySpecific(other);
    }

    public void addScoresFrom(H2HMatch matchFrom) {
        if (isAllParticipantsDefined() && matchFrom.isAllParticipantsDefined()) {
            Integer score1From = getParticipant1Id().equals(matchFrom.getParticipant1Id()) ? matchFrom.getScore1_1() : matchFrom.getScore1_2();
            Integer score2From = getParticipant1Id().equals(matchFrom.getParticipant1Id()) ? matchFrom.getScore1_2() : matchFrom.getScore1_1();

            if (score1From != null)
                setScore1_1(Util.convertEmptyIntegerToZero(getScore1_1()) + score1From);

            if (score2From != null)
                setScore1_2(Util.convertEmptyIntegerToZero(getScore1_2()) + score2From);

            addScoresFromSpecific(matchFrom);
        }
    }

    private boolean isAllParticipantsDefined() {
        return getParticipant1Id() != null && getParticipant2Id() != null;
    }
}
