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

    private CompSeasonPhaseKey compSeasonPhaseKey;

    public abstract int getSpecificId();
    abstract String[] getSpecificPropertiesInSQLStrings();
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
                QueryUtil.convertDateTimeToDbString(date)
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

    public CompSeasonPhaseKey getCompSeasonPhaseKey() {
        return compSeasonPhaseKey;
    }

    public void setCompSeasonPhaseKey(CompSeasonPhaseKey compSeasonPhaseKey) {
        this.compSeasonPhaseKey = compSeasonPhaseKey;
    }
}
