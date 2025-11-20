package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.calculation.StandingProcessor;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class H2HObjectFactory<PK extends CompSeasonParticipantKey,
        PPK extends CompSeasonPhaseParticipantKey,
        P extends Participant,
        CSP extends SuperKeyEntity,
        MK extends H2HMatchKey,
        M extends H2HMatch,
        MPK extends H2HMatchPartKey,
        MP extends H2HMatchPart,
        MPSK extends H2HMatchPartStatKey,
        MPS extends H2HMatchPartStat> {
    private H2HMatchManager<MK, M> manager;

    public abstract MK getKey(CompSeasonKey compSeasonKey, int specifId);
    public abstract M getMatch();
    public abstract StandingProcessor<PK, PPK, P, CSP, MK, M, MPK, MP, MPSK, MPS> getStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) throws SQLException;
    public abstract H2HPartObjectFactory<MPK, MP, MPSK, MPS> getPartObjectFactory();
    public abstract String getProcessManagePath();
    abstract H2HMatchManager<MK, M> instantiateManager(Statement stat);

    public H2HMatchManager<MK, M> getManager(Statement stat) {
        if (manager == null)
            manager = instantiateManager(stat);

        return manager;
    }

    public M getInstance(Statement stat, CompSeasonKey compSeasonKey, int specifId) throws SQLException {
        return getManager(stat).getEntityFromSuperKey(getKey(compSeasonKey, specifId));
    }

    public ArrayList<M> getMatchesForParent(Statement stat, CompSeasonKey compSeasonKey, int specifId) throws SQLException {
        MK parentKey = getKey(compSeasonKey, specifId);
        List<M> matches = getManager(stat).getMatchesForParent(parentKey);

        return new ArrayList<>(matches.stream().map(this::castMatch).toList());
    }

    public void update(Statement stat, CompSeasonKey compSeasonKey, int specifId, H2HMatch h2hMatch) throws SQLException {
        getManager(stat).update(getKey(compSeasonKey, specifId), castMatch(h2hMatch));
    }

    public MK castKey(H2HMatchKey h2hMatchKey) {
        return getKey(h2hMatchKey.getSuperKey(), h2hMatchKey.getSpecificId());
    }

    public M castMatch(H2HMatch h2hMatch) {
        M newMatch = getMatch();
        h2hMatch.copy(newMatch);

        return newMatch;
    }
}
