package com.sportservlet.flush;

import com.sports.cache.key.*;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.key.ClientCompSeasonKey;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;
import com.sports.logic.factory.ParticipantType;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class H2HMatchFlusher extends CacheFlusher {
    private final H2HMatchKey h2HMatchKey;

    public H2HMatchFlusher(H2HMatchKey h2HMatchKey) {
        this.h2HMatchKey = h2HMatchKey;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) throws SQLException {
        CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> factory = new DbCalculation(stat).getCompSeasonParticipantFactory(h2HMatchKey.getCompetitionId());
        com.sports.calc.h2hsports.DbCalculation dbCalc = new com.sports.calc.h2hsports.DbCalculation(stat);
        H2HObjectFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> h2HObjectFactory = factory.getH2HObjectFactory();

        H2HMatch h2HMatch = h2HObjectFactory.getInstance(stat, h2HMatchKey.getSuperKey(), h2HMatchKey.getSpecificId());
        H2HMatch matchNextRound = dbCalc.getH2HMatchNextRound(h2HObjectFactory, h2HMatchKey.getSuperKey(), h2HMatch);

        return new ArrayList<>() {{
            addMatchListKeys(this, h2HMatch);

            StandingParticipantKey sp1Key = getStandingParticipantKey(factory, h2HMatch.getParticipant1Id());
            StandingParticipantKey sp2Key = getStandingParticipantKey(factory, h2HMatch.getParticipant2Id());

            if (sp1Key != null)
                add(sp1Key);

            if (sp2Key != null)
                add(sp2Key);

            if (matchNextRound != null) {
                addMatchListKeys(this, matchNextRound);
                addAll(replicateForClientsWithRights(
                        new PhaseParticipantListReplicator(matchNextRound.getCompSeasonPhaseId()),
                        h2HMatchKey.getSuperKey(), stat));
            }

            if (h2HMatch.getParticipant1Id() != null && h2HMatch.getParticipant2Id() != null)
                addAll(replicateForClientsWithRights(new EncounterListReplicator(
                        h2HMatch.getParticipant1Id(), h2HMatch.getParticipant2Id(), factory.getParticipantType()),
                        h2HMatchKey.getSuperKey(), stat));
        }};
    }

    private void addMatchListKeys(List<CacheKey> cacheKeys, H2HMatch h2HMatch) {
        MatchListPhaseParticipantKey pp1Key = getMatchListPhaseParticipantKey(h2HMatch.getCompSeasonPhaseId(),
                h2HMatch.getParticipant1Id());
        MatchListPhaseParticipantKey pp2Key = getMatchListPhaseParticipantKey(h2HMatch.getCompSeasonPhaseId(),
                h2HMatch.getParticipant2Id());

        if (pp1Key != null)
            cacheKeys.add(pp1Key);

        if (pp2Key != null)
            cacheKeys.add(pp2Key);
    }

    private StandingParticipantKey getStandingParticipantKey(CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, Integer participantId) {
        if (participantId != null) {
            CompSeasonParticipantKey cspKey = factory.getCompSeasonParticKey(h2HMatchKey.getSuperKey(), participantId);
            return new StandingParticipantKey(cspKey);
        }

        return null;
    }

    private MatchListPhaseParticipantKey getMatchListPhaseParticipantKey(int compSeasonPhaseId, Integer participantId) {
        if (participantId != null)
            return new MatchListPhaseParticipantKey(h2HMatchKey.getCompetitionId(), h2HMatchKey.getSeasonId(),
                    compSeasonPhaseId, participantId);

        return null;
    }

    private static class PhaseParticipantListReplicator extends ClientReplicator {
        private final int compSeasonPhaseId;

        public PhaseParticipantListReplicator(int compSeasonPhaseId) {
            this.compSeasonPhaseId = compSeasonPhaseId;
        }

        @Override
        CacheKey getCacheKeyForClientCompSeason(ClientCompSeasonKey clientCompSeasonKey) {
            return new CompSeasonPhaseParticipantListKey(clientCompSeasonKey.getCompetitionId(),
                    clientCompSeasonKey.getSeasonId(),
                    compSeasonPhaseId,
                    clientCompSeasonKey.getClientId()
            );
        }
    }

    private static class EncounterListReplicator extends ClientReplicator {
        private final int participantIdMin;
        private final int participantIdMax;
        private final ParticipantType participantType;

        public EncounterListReplicator(int participant1Id, int participant2Id, ParticipantType participantType) {
            this.participantIdMin = Math.min(participant1Id, participant2Id);
            this.participantIdMax = Math.max(participant1Id, participant2Id);
            this.participantType = participantType;
        }

        @Override
        CacheKey getCacheKeyForClientCompSeason(ClientCompSeasonKey clientCompSeasonKey) {
            int clientId = clientCompSeasonKey.getClientId();

            switch (participantType) {
                case PERSON_SPORT -> {
                    return new EncounterListPersonSportKey(participantIdMin, participantIdMax, clientId);
                }
                case DOUBLE -> {
                    return new EncounterListDoubleKey(participantIdMin, participantIdMax, clientId);
                }
                default -> {
                    return new EncounterListTeamKey(participantIdMin, participantIdMax, clientId);
                }
            }
        }
    }
}
