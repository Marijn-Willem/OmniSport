package com.sports.logic.calculation;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.H2HMatchPartStatManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

abstract class CompSeasonParticipantDedoubler<PK extends CompSeasonParticipantKey,
        PPK extends CompSeasonPhaseParticipantKey,
        P extends Participant,
        CSP extends SuperKeyEntity,
        MK extends H2HMatchKey,
        M extends H2HMatch,
        MPK extends H2HMatchPartKey,
        MP extends H2HMatchPart,
        MPSK extends H2HMatchPartStatKey,
        MPS extends H2HMatchPartStat> {
    Statement stat;
    List<PK> cspKeysFrom;

    abstract CompSeasonParticipantFactory<PK, PPK, P, CSP, MK, M, MPK, MP, MPSK, MPS> getFactory();

    CompSeasonParticipantDedoubler(Statement stat) {
        this.stat = stat;
    }

    void dedoubleSpecific(int pFromId, int pToId) throws SQLException {

    }

    public void dedouble(int pFromId, int pToId) throws SQLException {
        CompSeasonParticipantFactory<PK, PPK, P, CSP, MK, M, MPK, MP, MPSK, MPS> factory = getFactory();
        H2HObjectFactory<PK, PPK, P, CSP, MK, M, MPK, MP, MPSK, MPS> h2hObjectFactory = factory.getH2HObjectFactory();

        CompSeasonParticipantManager<PK, CSP> cspm = factory.getCompSeasonParticipantManager(stat);
        CompSeasonPhaseParticipantManager<PK, PPK> csppm = factory.getPhaseParticManager(stat);
        H2HMatchManager<MK, M> mm = h2hObjectFactory.getManager(stat);
        H2HMatchPartStatManager<MPSK, MPS> mpsm = h2hObjectFactory.getPartObjectFactory().getPartStatObjectFactory().getStatManager(stat);

        cspKeysFrom = cspm.getCompSeasonsFromParticipant(pFromId);
        List<PPK> csppKeysFrom = csppm.getPhaseParticipants(cspKeysFrom);
        Map<MK, M> mMap = mm.getMatchesForPhaseParticipants(csppKeysFrom);
        Map<MPSK, MPS> mpsMap = mpsm.getH2HMatchPartStatMap(cspKeysFrom);

        Set<PK> cspKeysToExisting = new HashSet<>(cspm.getCompSeasonsFromParticipant(pToId));
        List<PK> cspKeysTo = new ArrayList<>();

        for (PK cspKeyFrom : cspKeysFrom) {
            PK keyTo = factory.getCompSeasonParticKey(cspKeyFrom.getSuperKey(), pToId);
            if (!cspKeysToExisting.contains(keyTo))
                cspKeysTo.add(keyTo);
        }

        List<PPK> csppKeysTo = new ArrayList<>();

        for (PPK csppKeyFrom : csppKeysFrom)
            csppKeysTo.add(factory.getPhaseParticKey(csppKeyFrom.getSuperKey(), pToId));

        cspm.insertCompSeasonParticipants(cspKeysTo);
        csppm.insertPhaseParticipantKeyList(csppKeysTo);

        for (Map.Entry<MK, M> me : mMap.entrySet())
            if (me.getValue().getParticipant1Id() == pFromId)
                me.getValue().setParticipant1Id(pToId);
            else
                me.getValue().setParticipant2Id(pToId);

        mm.updateMatchMap(mMap);

        for (Map.Entry<MPSK, MPS> me : mpsMap.entrySet())
            me.getValue().setParticipantId(pToId);

        mpsm.updateMatchPartStatMap(mpsMap);

        dedoubleSpecific(pFromId, pToId);

        csppm.deletePhaseParticipants(csppKeysFrom);
        cspm.deleteCompSeasonParticipants(cspKeysFrom);
    }
}
