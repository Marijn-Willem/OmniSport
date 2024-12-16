package com.sports.logic.calculation;

import com.sports.entity.*;
import com.sports.entity.comparator.MatchDate;
import com.sports.entity.comparator.ParticipantStanding;
import com.sports.entity.comparator.SuperComparator;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class StandingProcessor<PK extends CompSeasonParticipantKey,
        PPK extends CompSeasonPhaseParticipantKey,
        P extends Participant,
        CSP extends SuperKeyEntity,
        MK extends H2HMatchKey,
        M extends H2HMatch,
        MPK extends H2HMatchPartKey,
        MP extends H2HMatchPart,
        MPSK extends H2HMatchPartStatKey,
        MPS extends H2HMatchPartStat> {
    private final CompSeasonParticipantFactory<PK, PPK, P, CSP, MK, M, MPK, MP, MPSK, MPS> factory;
    private final SnapshotCreationChecker singleStandingChecker = new SnapshotCreationChecker() {
        @Override
        boolean checkCreate(M m1, M m2) {
            return false;
        }

        @Override
        StandingContext<P> getStandingContext(M match, List<P> standing) {
            return new StandingContext<>(null, standing);
        }
    };
    private final SnapshotCreationChecker standingPerDateChecker = new SnapshotCreationChecker() {
        @Override
        boolean checkCreate(M m1, M m2) {
            return !Util.compareNullableObjects(m1.getDate(), m2.getDate());
        }

        @Override
        StandingContext<P> getStandingContext(M match, List<P> standing) {
            return new StandingContext<>(match.getDate(), standing);
        }
    };

    protected Statement stat;
    protected CompSeasonPhaseKey cspk;
    protected Map<Integer, P> particMap;
    protected List<M> h2HMatches;

    protected abstract CompSeasonParticipantFactory<PK, PPK, P, CSP, MK, M, MPK, MP, MPSK, MPS> getFactory();

    StandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        this.stat = stat;
        this.cspk = cspk;

        factory = getFactory();
    }

    public int getPointsWin() {
        return 2;
    }

    public int getPointsDraw() {
        return 1;
    }

    protected SuperComparator<P> getComparator() throws SQLException {
        return new ParticipantStanding<>();
    }

    protected void processSpecificSnapshot(int curMatchSort) throws SQLException {}

    public StandingContext<P> getStanding() throws SQLException {
        List<StandingContext<P>> standingSnapshots = getStandingSnapshots(singleStandingChecker);

        return standingSnapshots.size() == 1 ? standingSnapshots.get(0) : new StandingContext<>(null, new ArrayList<>());
    }

    public List<StandingContext<P>> getStandingsPerDate() throws SQLException {
        return getStandingSnapshots(standingPerDateChecker);
    }

    protected void setParticipantsAndMatches() throws SQLException {
        CompSeasonPhaseParticipantManager<PK, PPK> csppm = factory.getPhaseParticManager(stat);
        ParticipantManager<P> pm = factory.getParticipantManager(stat);
        H2HMatchManager<MK, M> h2hMM = factory.getH2HObjectFactory().getManager(stat);

        List<Integer> particIds = csppm.getParticipantsInCompSeasonPhases(Collections.singletonList(cspk));
        particMap = pm.getParticipantMap(particIds);
        particMap.forEach((k, v) -> v.setPoints(0));
        h2HMatches = h2hMM.getPlayedMatchesInCompSeasonPhase(cspk);
        h2HMatches.sort(new MatchDate());
    }

    protected void processH2HMatch(M h2HMatch) {
        if (h2HMatch.getScore1_1() != null && h2HMatch.getScore1_2() != null) {
            P partic1 = particMap.get(h2HMatch.getParticipant1Id());
            P partic2 = particMap.get(h2HMatch.getParticipant2Id());

            if (partic1 != null)
                processParticipant(h2HMatch, partic1, true);

            if (partic2 != null)
                processParticipant(h2HMatch, partic2, false);
        }
    }

    private List<StandingContext<P>> getStandingSnapshots(SnapshotCreationChecker checker) throws SQLException {
        List<StandingContext<P>> snapshots = new ArrayList<>();

        setParticipantsAndMatches();

        M prevMatch = null;

        for (int i = 0; i < h2HMatches.size(); i++) {
            M h2HMatch = h2HMatches.get(i);

            if (prevMatch != null && checker.checkCreate(prevMatch, h2HMatch))
                snapshots.add(getSnapshot(i - 1, checker));

            processH2HMatch(h2HMatch);
            prevMatch = h2HMatch;
        }

        if (!h2HMatches.isEmpty())
            snapshots.add(getSnapshot(h2HMatches.size() - 1, checker));

        return snapshots;
    }

    private StandingContext<P> getSnapshot(int curMatchSort, SnapshotCreationChecker checker) throws SQLException {
        processSpecificSnapshot(curMatchSort);
        List<P> standing = new ArrayList<>() {{
            for (Map.Entry<Integer, P> me : particMap.entrySet())
                add(factory.getCopyForStanding(me.getValue()));
        }};

        Calculation.sortParticipantsAndSetRankBasedFields(standing, getComparator());

        return checker.getStandingContext(h2HMatches.get(curMatchSort), standing);
    }

    private void processParticipant(M h2HMatch, P participant, boolean isP1) {
        participant.addPlayed(1);
        participant.addScore(isP1 ? h2HMatch.getScore1_1() : h2HMatch.getScore1_2());
        participant.addScoreAgainst(isP1 ? h2HMatch.getScore1_2() : h2HMatch.getScore1_1());

        int scoreDiff = (isP1 ? 1 : -1) * (h2HMatch.getScore1_1() - h2HMatch.getScore1_2());

        participant.addPoints(getPoints(scoreDiff));
        participant.setStreak(getNewStreak(scoreDiff, participant.getStreak()));

        if (scoreDiff > 0)
            participant.addWin();
        else if (scoreDiff < 0)
            participant.addLoss();
        else
            participant.addDraw();
    }

    private int getPoints(int scoreDiff) {
        return switch ((int) Math.signum(scoreDiff)) {
            case -1 -> 0;
            case 0 -> getPointsDraw();
            default -> getPointsWin();
        };
    }

    private int getNewStreak(int scoreDiff, int currentStreak) {
        int scoreDiffSig = (int)Math.signum(scoreDiff);
        int streakSig = (int)Math.signum(currentStreak);

        return (scoreDiffSig == streakSig ? currentStreak : 0) + scoreDiffSig;
    }

    private abstract class SnapshotCreationChecker {
        abstract boolean checkCreate(M m1, M m2);
        abstract StandingContext<P> getStandingContext(M match, List<P> standing);
    }
}
