package com.sports.calc.alcifo;

import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.AlcifoParticipant;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.AlcifoParticipantKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.entity.manager.ParticipantManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AlcifoPartParticipantFactory<CSPK extends CompSeasonParticipantKey,
        CSP extends SuperKeyEntity,
        P extends Participant,
        APK extends AlcifoParticipantKey,
        AP extends AlcifoParticipant,
        APPK extends SuperKey,
        PK extends SuperKey,
        APP extends AlcifoPartParticipant,
        APPKP extends SuperKey,
        APPP extends AlcifoPartParticipant> {
    private AlcifoPartParticipantManager<APPK, PK, APP> manager;

    private AlcifoPartParticipantManager<APPK, PK, APP> getCachedManager(Statement stat) {
        if (manager == null)
            manager = getManager(stat);

        return manager;
    }

    public abstract AlcifoPartParticipantManager<APPK, PK, APP> getManager(Statement stat);
    public abstract ParticipantManager<P> getParticipantManager(Statement stat);
    public abstract APPK getKey(PK partKey, int participantId);
    public abstract CompSeasonEventPartKey getCompSeasonEventPartKey(PK partKey);
    public abstract APP getInstance();
    public abstract AlcifoParticipantFactory<CSPK, CSP, P, APK, AP, APPKP, APPP> getParticipantFactory();

    public void insertPartParticipants(Statement stat, PK partKey, Map<APK, AP> participantMap) throws SQLException {
        Map<APPK, APP> partParticipantMap = new HashMap<>() {{
            participantMap.forEach((k, v) -> {
                if (v.getNoCountResultId() == null)
                    put(getKey(partKey, k.getParticipantId()), getInstance());
            });
        }};

        getCachedManager(stat).insertPartParticipantMap(partParticipantMap);
    }

    public void updateParticipants(Statement stat, PK partKey, List<APP> partParticipants) throws SQLException {
        Map<APPK, APP> updateMap = new HashMap<>() {{
            partParticipants.forEach(x -> put(getKey(partKey, x.getParticipantId()), x));
        }};

        getCachedManager(stat).updatePartParticipantMap(updateMap);
    }
}
