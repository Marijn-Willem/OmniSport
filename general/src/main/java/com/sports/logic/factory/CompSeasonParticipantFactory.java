package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.ParticipantManager;

import java.sql.Statement;

public interface CompSeasonParticipantFactory<PK extends CompSeasonParticipantKey,
        PPK extends CompSeasonPhaseParticipantKey,
        P extends Participant,
        CSP extends SuperKeyEntity,
        CSPP extends SuperKeyEntity,
        MK extends H2HMatchKey,
        M extends H2HMatch,
        MPK extends H2HMatchPartKey,
        MP extends H2HMatchPart,
        MPSK extends H2HMatchPartStatKey,
        MPS extends H2HMatchPartStat> {
    H2HObjectFactory<PK, PPK, P, CSP, CSPP, MK, M, MPK, MP, MPSK, MPS> getH2HObjectFactory();
    CompSeasonParticipantManager<PK, CSP> getCompSeasonParticipantManager(Statement stat);
    ParticipantManager<P> getParticipantManager(Statement stat);
    CompSeasonPhaseParticipantManager<PK, PPK, CSPP> getPhaseParticManager(Statement stat);
    PK getCompSeasonParticKey(CompSeasonKey csk, int specifId);
    PPK getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId);
    ParticipantType getParticipantType();
    String getParticipantDescription();
}
