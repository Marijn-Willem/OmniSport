package com.sports.calc.h2hsports;

import com.sports.entity.*;
import com.sports.entity.comparator.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public record DbCalculation(Statement stat) {
    public <MK extends H2HMatchKey, M extends H2HMatch> M retrieveH2HMatch(H2HObjectFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            MK,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, CompSeasonKey compSeasonKey, int specificId) throws SQLException {
        return factory.getInstance(stat, compSeasonKey, specificId);
    }

    public List<List<List<H2HMatch>>> getMatchMatrixCompSeasonPhase(CompSeasonPhaseKey cspk, List<? extends Participant> participantsSorted)
            throws SQLException {
        Map<Integer, Integer> pIndXMap = new HashMap<>();

        for (int i = 0; i < participantsSorted.size(); i++)
            pIndXMap.put(participantsSorted.get(i).getId(), i);

        H2HMatchManager<? extends H2HMatchKey, ? extends H2HMatch> mm =
                new com.sports.logic.calculation.DbCalculation(stat)
                        .getCompSeasonParticipantFactory(cspk.getCompetitionId())
                        .getH2HObjectFactory().getManager(stat);

        List<? extends H2HMatch> h2hMatchList = mm.getPlayedMatchesInCompSeasonPhase(cspk);

        List<List<List<H2HMatch>>> matrix = new ArrayList<>();

        for (int i = 0; i < participantsSorted.size(); i++) {
            List<List<H2HMatch>> matrixEntry = new ArrayList<>();

            for (int j = 0; j < participantsSorted.size(); j++)
                matrixEntry.add(new ArrayList<>());

            matrix.add(matrixEntry);
        }

        for (H2HMatch h2HMatch1 : h2hMatchList) {
            int p1IndX = pIndXMap.get(h2HMatch1.getParticipant1Id());
            int p2IndX = pIndXMap.get(h2HMatch1.getParticipant2Id());

            matrix.get(p1IndX).get(p2IndX).add(h2HMatch1);
        }

        return matrix;
    }

    public <MK extends H2HMatchKey, M extends H2HMatch> void processFinishH2HMatch(
            CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            MK,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, H2HMatchKey h2hmk, H2HMatch h2hm) throws SQLException {
        H2HObjectFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                MK,
                M,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> h2HObjectFactory = factory.getH2HObjectFactory();

        Map<MK, M> matchMap = new HashMap<>();
        matchMap.put(h2HObjectFactory.castKey(h2hmk), h2HObjectFactory.castMatch(h2hm));

        processFinishH2HMatches(factory, matchMap);
    }

    public <MK extends H2HMatchKey, M extends H2HMatch> void processFinishH2HMatches(
            CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            MK,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, Map<MK, M> matchMap)
            throws SQLException {
        if (!matchMap.isEmpty()) {
            List<M> matchList = matchMap.values().stream().toList();

            H2HObjectFactory<? extends CompSeasonParticipantKey,
                    ? extends CompSeasonPhaseParticipantKey,
                    ? extends Participant,
                    ? extends SuperKeyEntity,
                    MK,
                    M,
                    ? extends H2HMatchPartKey,
                    ? extends H2HMatchPart,
                    ? extends H2HMatchPartStatKey,
                    ? extends H2HMatchPartStat> h2HObjectFactory = factory.getH2HObjectFactory();
            H2HMatchManager<MK, M> h2hmm = h2HObjectFactory.getManager(stat);

            CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);

            Map<CompSeasonPhaseKey, CompSeasonPhase> compSeasonPhaseMap = cspm.getCompSeasonPhaseMap(
                    matchList.stream().map(H2HMatch::getCompSeasonPhaseKey).toList());
            setHasKnockoutParent(compSeasonPhaseMap.values().stream().toList(), cspm);

            for (M h2hm : matchList) {
                h2hm.setFinished(true);

                CompSeasonPhase csp = compSeasonPhaseMap.get(h2hm.getCompSeasonPhaseKey());

                if (h2hm.getParentMatchId() == null && csp.isHasKnockoutParent())
                    processFinishH2HMatchKnockout(factory, h2hm, csp, cspm);
            }

            h2hmm.updateMatchMap(matchMap);

            for (Map.Entry<CompSeasonPhaseKey, CompSeasonPhase> me : compSeasonPhaseMap.entrySet())
                if (me.getValue().isHasKnockoutParent())
                    processFinishCompSeasonPhase(h2hmm, me.getKey(), cspm);

            List<M> nonParentMatches = matchList.stream().filter(x ->
                    !isParent(x, compSeasonPhaseMap.get(x.getCompSeasonPhaseKey()))).toList();

            updateElos(factory, nonParentMatches);
        }
    }

    public <MK extends H2HMatchKey, M extends H2HMatch> void createKnockoutMatches(H2HObjectFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            MK,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, CompSeasonPhaseKey knockoutPhaseKey)
            throws SQLException {
        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);

        List<CompSeasonPhase> roundPhases = cspm.getPhaseListFromParent(knockoutPhaseKey);
        setPhaseDescriptionsFromTypes(roundPhases);

        roundPhases.sort(new CompSeasonPhaseRoundDescription());

        CompSeasonKey csk = knockoutPhaseKey.getSuperKey();

        List<CompSeasonPhaseKey> phaseKeys = new ArrayList<>();

        for (CompSeasonPhase roundPhase : roundPhases)
            phaseKeys.add(new CompSeasonPhaseKey(csk, roundPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId()));

        H2HMatchManager<MK, M> h2hmm = factory.getManager(stat);
        h2hmm.deleteMatchesFromCompSeasonPhases(phaseKeys);

        CompSeasonPhase csp = cspm.getCompSeasonPhase(knockoutPhaseKey);

        int minKOOrder = 1;
        int maxKOOrder = (int) Math.pow(2, csp.getExpandFactor() * roundPhases.size() - 1);

        for (CompSeasonPhase roundPhase : roundPhases) {
            for (int i = minKOOrder; i <= maxKOOrder; i++) {
                M h2hMatch = factory.getMatch();
                h2hMatch.setCompSeasonPhaseId(roundPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId());
                h2hMatch.setKnockoutOrder(i);

                int newMatchId = h2hmm.getNewSpecId(csk);
                h2hmm.insert(factory.getKey(csk, newMatchId), h2hMatch);
            }

            minKOOrder = maxKOOrder + 1;
            maxKOOrder = maxKOOrder | (maxKOOrder >> 1);
        }
    }

    public CompSeasonPhase getFirstRoundFromParent(CompSeasonPhaseKey parentKey) throws SQLException {
        return getFirstRoundFromParent(parentKey, new CompSeasonPhaseManager(stat));
    }

    /**
     * Sets the canBeDeleted flag on a list of compseasonphases. The compseasonphases must belong to the same
     * compseason.
     *
     * @param compSeasonPhases The list of compseasonphases
     * @throws SQLException if a query fails
     */
    public void setCanBeDeleted(List<CompSeasonPhase> compSeasonPhases) throws SQLException {
        if (!compSeasonPhases.isEmpty()) {
            CompSeasonKey csk = compSeasonPhases.get(0).getCompSeasonPhaseKey().getSuperKey();

            List<CompSeasonPhaseKey> keys = new ArrayList<>();
            Set<CompSeasonPhaseKey> keySet = new HashSet<>();

            for (CompSeasonPhase compSeasonPhase : compSeasonPhases)
                keys.add(compSeasonPhase.getCompSeasonPhaseKey());

            H2HMatchManager<? extends H2HMatchKey, ? extends H2HMatch> mm = new com.sports.logic.calculation.DbCalculation(stat)
                    .getCompSeasonParticipantFactory(csk.getCompetitionId())
                    .getH2HObjectFactory().getManager(stat);

            List<? extends H2HMatch> h2HMatches = mm.getH2HMatchesFromCompSeasonPhases(keys);

            for (H2HMatch h2HMatch : h2HMatches)
                keySet.add(new CompSeasonPhaseKey(csk, h2HMatch.getCompSeasonPhaseId()));

            List<CompSeasonPhase> childPhases = new CompSeasonPhaseManager(stat).getChildCompSeasonPhases(keys);

            for (CompSeasonPhase childPhase : childPhases)
                keySet.add(childPhase.getParentPhaseKey());

            for (CompSeasonPhase compSeasonPhase : compSeasonPhases)
                if (!keySet.contains(compSeasonPhase.getCompSeasonPhaseKey()))
                    compSeasonPhase.setCanBeDeleted(true);
        }
    }

    public void setPhaseDescriptionsFromTypes(List<CompSeasonPhase> compSeasonPhases) throws SQLException {
        List<Integer> phaseTypeIds = compSeasonPhases.stream().map(CompSeasonPhase::getPhaseTypeId)
                .collect(Collectors.toList());

        Map<Integer, PhaseType> phaseTypeMap = new PhaseTypeManager(stat).getPhaseTypeMap(phaseTypeIds);

        compSeasonPhases.forEach(x -> x.setDescription(phaseTypeMap.get(x.getPhaseTypeId()).getName()));
    }

    public <M extends H2HMatch> M getH2HMatchNextRound(H2HObjectFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, CompSeasonKey csk, H2HMatch h2hMatch) throws SQLException {
        if (h2hMatch.getKnockoutOrder() != null) {
            CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);

            CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(csk, h2hMatch.getCompSeasonPhaseId());
            CompSeasonPhase csp = cspm.getCompSeasonPhase(cspk);
            CompSeasonPhase cspParent = cspm.getCompSeasonPhase(csp.getParentPhaseKey());
            List<CompSeasonPhase> siblingPhases = cspm.getPhaseListFromParent(cspParent.getCompSeasonPhaseKey());

            CompSeasonPhase nextPhase = getCompSeasonPhaseNextRound(siblingPhases, csp, cspm);

            if (nextPhase != null && nextPhase.getParentPhaseKey().equals(cspParent.getCompSeasonPhaseKey())) {
                int knockoutOrder = h2hMatch.getKnockoutOrder();
                int rounds = siblingPhases.size() * (cspParent.getExpandFactor() != null ? cspParent.getExpandFactor() : 1);

                int lastKOOrderCurrentRound = getLastKnockoutOrderCurrentRound(rounds, knockoutOrder);
                int lastKOOrderNextRound = lastKOOrderCurrentRound | (lastKOOrderCurrentRound >> 1);
                int nextKnockoutOrder = lastKOOrderNextRound - (lastKOOrderCurrentRound - knockoutOrder) / 2;

                List<M> h2hMatches = factory.getManager(stat).getH2HMatchList(csk, "compseasonphaseid = " +
                        nextPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId() +
                        " AND knockoutorder = " + nextKnockoutOrder);

                if (h2hMatches.size() == 1)
                    return h2hMatches.get(0);
            }
        }

        return null;
    }

    public Map<CompSeasonKey, List<H2HMatch>> getEncountersBetweenParticipants(
            CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, int participant1Id, int participant2Id) throws SQLException {
        Set<CompSeasonKey> compSeasonKeys = factory.getCompSeasonParticipantManager(stat).getCompSeasonsForParticipants(
                new ArrayList<>() {{ add(participant1Id); add(participant2Id); }}
        );

        List<? extends H2HMatch> h2HMatches = factory.getH2HObjectFactory().getManager(stat)
                .getMatchesWithBothParticipants(compSeasonKeys, participant1Id, participant2Id);

        Map<CompSeasonKey, List<H2HMatch>> encounterMap = new HashMap<>() {{
            h2HMatches.forEach(x -> {
                CompSeasonKey csKey = x.getCompSeasonPhaseKey().getSuperKey();

                if (!containsKey(csKey))
                    put(csKey, new ArrayList<>());

                get(csKey).add(x);
            });
        }};

        Comparator<H2HMatch> comparator = new MatchDate();

        encounterMap.values().forEach(x -> x.sort(comparator));

        return encounterMap;
    }

    public List<Participant> getKnockoutPhaseRanking(CompSeasonPhaseKey cspk,
                                                     CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                                                             ? extends CompSeasonPhaseParticipantKey,
                                                             ? extends Participant,
                                                             ? extends SuperKeyEntity,
                                                             ? extends H2HMatchKey,
                                                             ? extends H2HMatch,
                                                             ? extends H2HMatchPartKey,
                                                             ? extends H2HMatchPart,
                                                             ? extends H2HMatchPartStatKey,
                                                             ? extends H2HMatchPartStat> factory)
            throws SQLException {
        List<Participant> ranking = new ArrayList<>();

        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);
        CompSeasonPhase csp = cspm.getCompSeasonPhase(cspk);

        if (csp.isKnockoutParent() && csp.isFinished() && csp.getExpandFactor() == 1) {
            List<CompSeasonPhase> childPhases = cspm.getPhaseListFromParent(cspk);
            List<CompSeasonPhaseKey> phaseKeys = childPhases.stream().map(CompSeasonPhase::getCompSeasonPhaseKey)
                    .collect(Collectors.toList());

            H2HMatchManager<? extends H2HMatchKey, ? extends H2HMatch> h2HMm = factory.getH2HObjectFactory().getManager(stat);
            List<? extends H2HMatch> matches = h2HMm.getH2HMatchesFromCompSeasonPhases(phaseKeys);
            matches.sort(new H2HMatchKnockoutOrderDate());

            List<Integer> participantIds = new ArrayList<>();
            Map<Integer, List<H2HMatch>> h2HMatchMap = new HashMap<>();

            for (int i = matches.size() - 1; i >= 0; i--) {
                H2HMatch match = matches.get(i);
                int p1Id = match.getParticipant1Id();
                int p2Id = match.getParticipant2Id();

                participantIds.add(p1Id);
                participantIds.add(p2Id);

                if (!h2HMatchMap.containsKey(p1Id))
                    h2HMatchMap.put(p1Id, new ArrayList<>());

                if (!h2HMatchMap.containsKey(p2Id))
                    h2HMatchMap.put(p2Id, new ArrayList<>());

                h2HMatchMap.get(p1Id).add(match);
                h2HMatchMap.get(p2Id).add(match);
            }

            ParticipantManager<? extends Participant> pm = factory.getParticipantManager(stat);
            Map<Integer, ? extends Participant> participantMap = pm.getParticipantMap(participantIds);

            processKnockoutPhaseRanks(matches, h2HMatchMap, participantMap);
            ranking.addAll(participantMap.values());
            ranking.sort(new ParticipantRank());
        }

        return ranking;
    }

    private void setHasKnockoutParent(List<CompSeasonPhase> compSeasonPhases, CompSeasonPhaseManager cspm)
            throws SQLException {
        List<CompSeasonPhase> phasesWithParent = compSeasonPhases.stream()
                .filter(x -> x.getParentPhaseKey() != null).toList();

        Map<CompSeasonPhaseKey, CompSeasonPhase> parentPhaseMap = cspm.getCompSeasonPhaseMap(
            phasesWithParent.stream().map(CompSeasonPhase::getParentPhaseKey).collect(Collectors.toList())
        );

        phasesWithParent.forEach(x ->
            x.setHasKnockoutParent(parentPhaseMap.get(x.getParentPhaseKey()).isKnockoutParent())
        );
    }

    private boolean isParent(H2HMatch h2HMatch, CompSeasonPhase compSeasonPhase) {
        return compSeasonPhase.isHasParentMatches() && h2HMatch.getParentMatchId() == null;
    }

    private <M extends H2HMatch> void processFinishH2HMatchKnockout(
            CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, M h2hm, CompSeasonPhase csp, CompSeasonPhaseManager cspm)
            throws SQLException {
        List<CompSeasonPhase> compSeasonPhases = new com.sports.logic.calculation.DbCalculation(stat)
                .getCompSeasonPhaseSiblings(csp.getCompSeasonPhaseKey());
        CompSeasonPhase nextPhase = getCompSeasonPhaseNextRound(compSeasonPhases, csp, cspm);

        if (nextPhase != null) {
            CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey,
                    ? extends CompSeasonPhaseParticipantKey> csppm =
                    factory.getPhaseParticManager(stat);
            csppm.insert(factory.getPhaseParticKey(nextPhase.getCompSeasonPhaseKey(), h2hm.getWinnerId()));

            if (nextPhase.getParentPhaseId().equals(csp.getParentPhaseId())) {
                int knockoutOrder = h2hm.getKnockoutOrder();
                int oppMatchKOOrder = knockoutOrder + (knockoutOrder % 2 == 0 ? -1 : 1);

                H2HObjectFactory<? extends CompSeasonParticipantKey,
                        ? extends CompSeasonPhaseParticipantKey,
                        ? extends Participant,
                        ? extends SuperKeyEntity,
                        ? extends H2HMatchKey,
                        M,
                        ? extends H2HMatchPartKey,
                        ? extends H2HMatchPart,
                        ? extends H2HMatchPartStatKey,
                        ? extends H2HMatchPartStat> h2HObjectFactory = factory.getH2HObjectFactory();
                H2HMatchManager<? extends H2HMatchKey, ? extends H2HMatch> h2hmm = h2HObjectFactory.getManager(stat);
                CompSeasonKey csk = csp.getCompSeasonPhaseKey().getSuperKey();

                List<? extends H2HMatch> h2hMatches = h2hmm.getH2HMatchList(csk, "compseasonphaseid = " +
                        h2hm.getCompSeasonPhaseId() + " AND knockoutorder = " + oppMatchKOOrder);
                M nextMatch = getH2HMatchNextRound(h2HObjectFactory, csk, h2hm);

                if (h2hMatches.size() == 1 && nextMatch != null) {
                    H2HMatch oppH2HMatch = h2hMatches.get(0);

                    H2HMatch h2hMatch1 = knockoutOrder % 2 == 0 ? oppH2HMatch : h2hm;
                    H2HMatch h2hMatch2 = knockoutOrder % 2 == 0 ? h2hm : oppH2HMatch;

                    if (h2hMatch1.isFinished())
                        nextMatch.setParticipant1Id(h2hMatch1.getWinnerId());

                    if (h2hMatch2.isFinished())
                        nextMatch.setParticipant2Id(h2hMatch2.getWinnerId());

                    h2HObjectFactory.update(stat, csk, nextMatch.getSpecificId(), nextMatch);
                }
            }
        }
    }

    private <MK extends H2HMatchKey, M extends H2HMatch> void processFinishCompSeasonPhase(H2HMatchManager<MK, M> mm,
                                                                                           CompSeasonPhaseKey cspk,
                                                                                           CompSeasonPhaseManager cspm)
            throws SQLException {
        List<M> h2HMatches = mm.getNonFinishedH2HMatches(cspk);

        if (h2HMatches.isEmpty()) {
            CompSeasonPhase csp = cspm.getCompSeasonPhase(cspk);
            CompSeasonPhase parentPhase = cspm.getCompSeasonPhase(new CompSeasonPhaseKey(cspk.getSuperKey(), csp.getParentPhaseId()));

            finishCompSeasonPhase(cspm, csp);
            processFinishParentPhase(cspm, parentPhase);
        }
    }

    private void processFinishParentPhase(CompSeasonPhaseManager cspm, CompSeasonPhase parentPhase) throws SQLException {
        CompSeasonPhaseKey cspk = parentPhase.getCompSeasonPhaseKey();
        List<CompSeasonPhase> childPhases = cspm.getNonFinishedPhasesFromParent(cspk);

        if (childPhases.isEmpty())
            finishCompSeasonPhase(cspm, parentPhase);
    }

    private void finishCompSeasonPhase(CompSeasonPhaseManager cspm, CompSeasonPhase csp) throws SQLException {
        csp.setFinished(true);
        cspm.updateCompSeasonPhase(csp.getCompSeasonPhaseKey(), csp);
    }

    private void updateElos(CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory, Collection<? extends H2HMatch> h2HMatches) throws SQLException {
        if (!h2HMatches.isEmpty()) {
            ParticipantManager<? extends Participant> pm = factory.getParticipantManager(stat);

            List<Integer> particIds = new ArrayList<>();

            for (H2HMatch h2HMatch : h2HMatches) {
                particIds.add(h2HMatch.getParticipant1Id());
                particIds.add(h2HMatch.getParticipant2Id());
            }

            Map<Integer, ? extends Participant> participants = pm.getParticipantMap(particIds);

            for (H2HMatch h2HMatch : h2HMatches) {
                Participant p1 = participants.get(h2HMatch.getParticipant1Id());
                Participant p2 = participants.get(h2HMatch.getParticipant2Id());

                Integer score1 = h2HMatch.getScore1_1();
                Integer score2 = h2HMatch.getScore1_2();

                if (score1 != null && score2 != null) {
                    double eloRes1 = score1 > score2 ? 1.0 : score1.equals(score2) ? 0.5 : 0.0;
                    double eloRes2 = 1.0 - eloRes1;

                    p1.setElo(getNewElo(eloRes1, p1.getElo(), p1.getElo() - p2.getElo()));
                    p2.setElo(getNewElo(eloRes2, p2.getElo(), p2.getElo() - p1.getElo()));
                }
            }

            pm.updateParticipantMap(participants);
        }
    }

    private CompSeasonPhase getCompSeasonPhaseNextRound(List<CompSeasonPhase> compSeasonPhases,
                                                        CompSeasonPhase cspRef,
                                                        CompSeasonPhaseManager cspm) throws SQLException {
        CompSeasonPhase nextPhase = null;

        for (CompSeasonPhase compSeasonPhase : compSeasonPhases)
            if (compSeasonPhase.getRound() == cspRef.getRound() + 1) {
                nextPhase = compSeasonPhase;
                break;
            }

        if (nextPhase == null) {
            // Find the first round of the next knockout parent phase
            List<CompSeasonPhase> parentPhases =
                    cspm.getKnockoutCompSeasonPhases(cspRef.getCompSeasonPhaseKey().getSuperKey());

            parentPhases.sort(new CompSeasonPhaseParentOrder());

            CompSeasonPhase nextParent = null;

            for (int i = 0; i < parentPhases.size() - 1; i++) {
                CompSeasonPhase parPhase = parentPhases.get(i);

                if (parPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId() == cspRef.getParentPhaseId()) {
                    CompSeasonPhase nextParentPhase = parentPhases.get(i + 1);

                    if (nextParentPhase.getParentOrder() != null &&
                            parPhase.getParentOrder() != null &&
                            nextParentPhase.getParentOrder() == parPhase.getParentOrder() + 1)
                        nextParent = parentPhases.get(i + 1);

                    break;
                }
            }

            if (nextParent != null)
                nextPhase = getFirstRoundFromParent(nextParent.getCompSeasonPhaseKey(), cspm);
        }

        return nextPhase;
    }

    private int getLastKnockoutOrderCurrentRound(int rounds, int knockoutOrder) {
        int lastId = (int) Math.pow(2, rounds - 1);

        while (lastId < knockoutOrder)
            lastId = lastId | (lastId >> 1);

        return lastId;
    }

    private int getNewElo(double result, int curElo, int eloDiff) {
        double divisor = 1.0 + Math.pow(10.0, -(1.0 * eloDiff) / 400.0);
        double resultExp = 1.0 / divisor;

        return curElo + (int) (24.0 * (result - resultExp));
    }

    private CompSeasonPhase getFirstRoundFromParent(CompSeasonPhaseKey parentKey, CompSeasonPhaseManager cspm)
            throws SQLException {
        List<CompSeasonPhase> compSeasonPhases = cspm.getPhaseListFromParent(parentKey);

        if (!compSeasonPhases.isEmpty()) {
            setPhaseDescriptionsFromTypes(compSeasonPhases);
            compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());
            return compSeasonPhases.get(0);
        }

        return null;
    }

    private void processKnockoutPhaseRanks(List<? extends H2HMatch> h2HMatches,
                                           Map<Integer, List< H2HMatch>> h2HMatchMap,
                                           Map<Integer, ? extends Participant> participantMap) {
        H2HMatch lastMatch = h2HMatches.get(h2HMatches.size() - 1);
        int rank1Id = lastMatch.getWinnerId();
        int rank2Id = lastMatch.getParticipant1Id() == rank1Id ? lastMatch.getParticipant2Id() : lastMatch.getParticipant1Id();

        participantMap.get(rank1Id).setRank(1);
        participantMap.get(rank2Id).setRank(2);

        Queue<Integer> participantQueue = new LinkedList<>();
        participantQueue.add(rank1Id);
        participantQueue.add(rank2Id);

        while (!participantQueue.isEmpty()) {
            int participantId = participantQueue.poll();
            processParticipantForPhaseRanks(participantId, participantQueue, h2HMatchMap, participantMap);
        }
    }

    private void processParticipantForPhaseRanks(int participantId,
                                                 Queue<Integer> participantQueue,
                                                 Map<Integer, List<H2HMatch>> h2HMatchMap,
                                                 Map<Integer, ? extends Participant> participantMap) {
        int rankWinner = participantMap.get(participantId).getRank();
        int rank = rankWinner;
        List<? extends H2HMatch> matches = h2HMatchMap.get(participantId);

        for (int i = 1; i < matches.size(); i++) {
            H2HMatch match = matches.get(i);
            int opponentId = match.getParticipant1Id() == participantId ? match.getParticipant2Id() : match.getParticipant1Id();
            rank = getNextRank(rankWinner, rank);
            participantMap.get(opponentId).setRank(rank);
            participantQueue.add(opponentId);
        }
    }

    private int getNextRank(int rankWinner, int rank) {
        int firstPower2 = 2;
        while (firstPower2 < rank)
            firstPower2 *= 2;

        return rankWinner + firstPower2;
    }
}
