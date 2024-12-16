package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.ParticipantManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class CompSeasonParticipantFactory<PK extends CompSeasonParticipantKey,
        PPK extends CompSeasonPhaseParticipantKey,
        P extends Participant,
        CSP extends SuperKeyEntity,
        MK extends H2HMatchKey,
        M extends H2HMatch,
        MPK extends H2HMatchPartKey,
        MP extends H2HMatchPart,
        MPSK extends H2HMatchPartStatKey,
        MPS extends H2HMatchPartStat> {
    private CompSeasonPhaseParticipantManager<PK, PPK> manager;

    private CompSeasonPhaseParticipantManager<PK, PPK> getCachedManager(Statement stat) {
        if (manager == null)
            manager = getPhaseParticManager(stat);

        return manager;
    }

    public abstract H2HObjectFactory<PK, PPK, P, CSP, MK, M, MPK, MP, MPSK, MPS> getH2HObjectFactory();
    public abstract CompSeasonParticipantManager<PK, CSP> getCompSeasonParticipantManager(Statement stat);
    public abstract ParticipantManager<P> getParticipantManager(Statement stat);
    public abstract CompSeasonPhaseParticipantManager<PK, PPK> getPhaseParticManager(Statement stat);
    public abstract PK getCompSeasonParticKey(CompSeasonKey csk, int specifId);
    public abstract PPK getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId);
    public abstract ParticipantType getParticipantType();
    public abstract String getParticipantDescription();
    public abstract P getCopyForStanding(P participant);

    public void deletePhaseParticipants(Statement stat, CompSeasonPhaseKey cspk, List<Integer> participantIds)
            throws SQLException  {
        getCachedManager(stat).deletePhaseParticipants(getPhaseParticipantKeys(cspk, participantIds));
    }

    public void insertPhaseParticipantKeyList(Statement stat, CompSeasonPhaseKey cspk, List<Integer> participantIds)
        throws SQLException  {
        getCachedManager(stat).insertPhaseParticipantKeyList(getPhaseParticipantKeys(cspk, participantIds));
    }

    private List<PPK> getPhaseParticipantKeys(CompSeasonPhaseKey cspk, List<Integer> participantIds) {
        return participantIds.stream().map(x -> getPhaseParticKey(cspk, x)).toList();
    }
}
