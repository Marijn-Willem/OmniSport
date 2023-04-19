package com.sports.logic.calculation;

import com.sports.entity.H2HMatch;
import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.CompSeasonPhaseParticipantKey;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.H2HMatchPartStatManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

abstract class CompSeasonParticipantDedoubler<S extends CompSeasonParticipantKey, T extends SuperKeyEntity> {
    Statement stat;
    List<S> cspKeysFrom;

    abstract CompSeasonParticipantFactory<S, T> getFactory();

    CompSeasonParticipantDedoubler(Statement stat) {
        this.stat = stat;
    }

    void dedoubleSpecific(int pFromId, int pToId) throws SQLException {

    }

    public void dedouble(int pFromId, int pToId) throws SQLException {
        CompSeasonParticipantFactory<S, T> factory = getFactory();
        H2HObjectFactory<? extends H2HMatchKey, ? extends H2HMatch> h2hObjectFactory = factory.getH2HObjectFactory();

        CompSeasonParticipantManager cspm = factory.getCompSeasonParticipantManager(stat);
        CompSeasonPhaseParticipantManager csppm = factory.getPhaseParticManager(stat);
        H2HMatchManager mm = h2hObjectFactory.getManager(stat);
        H2HMatchPartStatManager mpsm = h2hObjectFactory.getPartObjectFactory().getPartStatObjectFactory().getStatManager(stat);

        cspKeysFrom = cspm.getCompSeasonsFromParticipant(pFromId);
        List<? extends CompSeasonPhaseParticipantKey> csppKeysFrom = csppm.getPhaseParticipants(cspKeysFrom);
        Map<? extends H2HMatchKey, ? extends H2HMatch> mMap = mm.getMatchesForPhaseParticipants(csppKeysFrom);
        Map<? extends H2HMatchPartStatKey, ? extends H2HMatchPartStat> mpsMap = mpsm.getH2HMatchPartStatMap(cspKeysFrom);

        Set<CompSeasonParticipantKey> cspKeysToExisting = new HashSet<>(cspm.getCompSeasonsFromParticipant(pToId));
        List<CompSeasonParticipantKey> cspKeysTo = new ArrayList<>();

        for (CompSeasonParticipantKey cspKeyFrom : cspKeysFrom) {
            CompSeasonParticipantKey keyTo = factory.getCompSeasonParticKey(cspKeyFrom.getSuperKey(), pToId);
            if (!cspKeysToExisting.contains(keyTo))
                cspKeysTo.add(keyTo);
        }

        List<CompSeasonPhaseParticipantKey> csppKeysTo = new ArrayList<>();

        for (CompSeasonPhaseParticipantKey csppKeyFrom : csppKeysFrom)
            csppKeysTo.add(factory.getPhaseParticKey(csppKeyFrom.getSuperKey(), pToId));

        cspm.insertCompSeasonParticipants(cspKeysTo);
        csppm.insertPhaseParticipantKeyList(csppKeysTo);

        for (Map.Entry<? extends H2HMatchKey, ? extends H2HMatch> me : mMap.entrySet())
            if (me.getValue().getParticipant1Id() == pFromId)
                me.getValue().setParticipant1Id(pToId);
            else
                me.getValue().setParticipant2Id(pToId);

        mm.updateMatchMap(mMap);

        for (Map.Entry<? extends H2HMatchPartStatKey, ? extends H2HMatchPartStat> me : mpsMap.entrySet())
            me.getValue().setParticipantId(pToId);

        mpsm.updateMatchPartStatMap(mpsMap);

        dedoubleSpecific(pFromId, pToId);

        csppm.deletePhaseParticipants(csppKeysFrom);
        cspm.deleteCompSeasonParticipants(cspKeysFrom);
    }
}
