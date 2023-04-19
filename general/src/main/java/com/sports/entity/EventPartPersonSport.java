package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.time.LocalDateTime;

public class EventPartPersonSport extends AlcifoPartParticipant {
    private Integer heat;

    private int compSeasonEventPartId;
    private int personSportId;
    private String personSportDescription;
    private int sportEventPartOrder;
    private double resultPoints;
    private EventPersonSport eventPersonSport;
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

    public int getCompSeasonEventPartId() {
        return compSeasonEventPartId;
    }

    public void setCompSeasonEventPartId(int compSeasonEventPartId) {
        this.compSeasonEventPartId = compSeasonEventPartId;
    }

    public int getPersonSportId() {
        return personSportId;
    }

    public void setPersonSportId(int personSportId) {
        this.personSportId = personSportId;
    }

    public String getPersonSportDescription() {
        return personSportDescription;
    }

    public void setPersonSportDescription(String personSportDescription) {
        this.personSportDescription = personSportDescription;
    }

    public int getSportEventPartOrder() {
        return sportEventPartOrder;
    }

    public void setSportEventPartOrder(int sportEventPartOrder) {
        this.sportEventPartOrder = sportEventPartOrder;
    }

    public double getResultPoints() {
        return resultPoints;
    }

    public void addResultPoints(double resultPoints) {
        this.resultPoints += resultPoints;
    }

    public EventPersonSport getEventPersonSport() {
        return eventPersonSport;
    }

    public void setEventPersonSport(EventPersonSport eventPersonSport) {
        this.eventPersonSport = eventPersonSport;
    }

    public CompSeasonEventPart getCompSeasonEventPart() {
        return compSeasonEventPart;
    }

    public void setCompSeasonEventPart(CompSeasonEventPart compSeasonEventPart) {
        this.compSeasonEventPart = compSeasonEventPart;
    }
}
