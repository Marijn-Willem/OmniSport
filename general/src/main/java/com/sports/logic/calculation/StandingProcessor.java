package com.sports.logic.calculation;

import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.comparator.MatchDate;
import com.sports.entity.comparator.ParticipantStanding;
import com.sports.entity.comparator.SuperComparator;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class StandingProcessor<T extends Participant> {
    protected Statement stat;
    protected CompSeasonPhaseKey cspk;
    protected Map<Integer, T> particMap;
    protected List<H2HMatch> h2HMatches;

    protected abstract CompSeasonParticipantFactory getFactory();

    StandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        this.stat = stat;
        this.cspk = cspk;
    }

    public int getPointsWin() {
        return 2;
    }

    public int getPointsDraw() {
        return 1;
    }

    protected SuperComparator<T> getComparator() throws SQLException {
        return new ParticipantStanding<>();
    }

    protected void processSpecific() throws SQLException {}

    public List<T> getStanding() throws SQLException {
        List<T> standing = new ArrayList<>();

        setParticipantsAndMatches();
        h2HMatches.sort(new MatchDate());

        for (H2HMatch match : h2HMatches)
            processH2HMatch(match);

        processSpecific();

        for (Map.Entry<Integer, T> me : particMap.entrySet())
            standing.add(me.getValue());

        Calculation.sortParticipantsAndSetRankBasedFields(standing, getComparator());

        return standing;
    }

    protected void setParticipantsAndMatches() throws SQLException {
        CompSeasonParticipantFactory factory = getFactory();
        CompSeasonPhaseParticipantManager csppm = factory.getPhaseParticManager(stat);
        ParticipantManager pm = factory.getParticipantManager(stat);
        H2HMatchManager h2hMM = factory.getH2HObjectFactory().getManager(stat);

        List<Integer> particIds = csppm.getParticipantsInCompSeasonPhases(Collections.singletonList(cspk));
        particMap = pm.getParticipantMap(particIds);
        particMap.forEach((k, v) -> v.setPoints(0));
        h2HMatches = h2hMM.getPlayedMatchesInCompSeasonPhase(cspk);
    }

    protected void processH2HMatch(H2HMatch h2HMatch) {
        if (h2HMatch.getScore1_1() != null && h2HMatch.getScore1_2() != null) {
            T partic1 = particMap.get(h2HMatch.getParticipant1Id());
            T partic2 = particMap.get(h2HMatch.getParticipant2Id());

            if (partic1 != null)
                processParticipant(h2HMatch, partic1, true);

            if (partic2 != null)
                processParticipant(h2HMatch, partic2, false);
        }
    }

    private void processParticipant(H2HMatch h2HMatch, T participant, boolean isP1) {
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
        switch ((int)Math.signum(scoreDiff)) {
            case -1: return 0;
            case 0: return getPointsDraw();
            default: return getPointsWin();
        }
    }

    private int getNewStreak(int scoreDiff, int currentStreak) {
        int scoreDiffSig = (int)Math.signum(scoreDiff);
        int streakSig = (int)Math.signum(currentStreak);

        return (scoreDiffSig == streakSig ? currentStreak : 0) + scoreDiffSig;
    }
}
