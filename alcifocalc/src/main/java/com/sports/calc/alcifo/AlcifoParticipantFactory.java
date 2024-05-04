package com.sports.calc.alcifo;

import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.AlcifoParticipant;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.entity.manager.AlcifoParticipantManager;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.logic.factory.ParticipantType;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AlcifoParticipantFactory<CSPK extends CompSeasonParticipantKey,
        CSP extends SuperKeyEntity,
        P extends Participant,
        APK extends AlcifoParticipantKey,
        AP extends AlcifoParticipant,
        APPK extends SuperKey,
        APP extends AlcifoPartParticipant> {
    private AlcifoParticipantManager<APK, AP> manager;

    private AlcifoParticipantManager<APK, AP> getCachedManager(Statement stat) {
        if (manager == null)
            manager = getManager(stat);

        return manager;
    }

    public abstract AlcifoParticipantManager<APK, AP> getManager(Statement stat);
    public abstract CompSeasonParticipantManager<CSPK, CSP> getCompSeasonParticipantManager(Statement stat);
    public abstract APK getAlcifoParticipantKey(CompSeasonEventKey cseKey, int participantId);
    public abstract AP getAlcifoParticipant();
    public abstract AlcifoPartParticipantFactory<CSPK, CSP, P, APK, AP, APPK, CompSeasonEventPartKey, APP, APPK, APP> getEventPartParticipantFactory();
    public abstract ParticipantType getParticipantType();

    public void insertParticipants(Statement stat, CompSeasonEventKey cseKey, List<Integer> participantIds) throws SQLException {
        Map<APK, AP> participantMap = new HashMap<>() {{
            participantIds.forEach(x -> put(getAlcifoParticipantKey(cseKey, x), getAlcifoParticipant()));
        }};

        getCachedManager(stat).insertParticipantMap(participantMap);
    }
}
