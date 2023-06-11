package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.time.LocalDateTime;

public class EventPartPersonSport extends AlcifoPartParticipant {
    private Integer heat;

    private int personSportId;
    private int sportEventPartOrder;
    private CompSeasonEventPart compSeasonEventPart;

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(heat)
        };
    }

    public LocalDateTime getEventDate() {
        if (compSeasonEventPart != null)
            return compSeasonEventPart.getDate() != null ? compSeasonEventPart.getDate() :
                    compSeasonEventPart.getCompSeasonEndDate();

        return null;
    }

    @Override
    public int getParticipantId() {
        return getPersonSportId();
    }

    @Override
    public void setParticipantId(int participantId) {
        setPersonSportId(participantId);
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    public int getPersonSportId() {
        return personSportId;
    }

    public void setPersonSportId(int personSportId) {
        this.personSportId = personSportId;
    }

    public int getSportEventPartOrder() {
        return sportEventPartOrder;
    }

    public void setSportEventPartOrder(int sportEventPartOrder) {
        this.sportEventPartOrder = sportEventPartOrder;
    }


    public CompSeasonEventPart getCompSeasonEventPart() {
        return compSeasonEventPart;
    }

    public void setCompSeasonEventPart(CompSeasonEventPart compSeasonEventPart) {
        this.compSeasonEventPart = compSeasonEventPart;
    }
}
