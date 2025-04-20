package com.sports.calc.darts;

import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.*;
import com.sports.entity.comparator.H2HMatchDate;
import com.sports.entity.comparator.ParticipantStanding;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.manager.*;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.calculation.StandingContext;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

public record DbCalculation(Statement stat) {
    public StatObject getSetWithStats(PersonMatchPartKey setKey) throws SQLException {
        PersonMatch personMatch = new PersonMatchManager(stat).getPersonMatch((PersonMatchKey) setKey.getSuperKey());

        return personMatch != null ? getSetWithStats(setKey, personMatch.getPersonSport1Id()) : null;
    }

    public StatObject getSetWithStats(PersonMatchPartKey setKey, int personSport1Id) throws SQLException {
        PersonMatchPartManager pmpm = new PersonMatchPartManager(stat);

        List<PersonMatchPart> personMatchParts =
                pmpm.getPersonMatchPartsFromParents(Collections.singletonList(setKey));

        List<PersonMatchPartKey> personMatchPartKeys = new ArrayList<>();

        StatObject statObject = new StatObject();

        for (PersonMatchPart personMatchPart : personMatchParts) {
            personMatchPartKeys.add(new PersonMatchPartKey((PersonMatchKey) setKey.getSuperKey(), personMatchPart.getPersonMatchPartId()));
            if (personMatchPart.isFinished())
                if (personMatchPart.isPerson1Win()) {
                    statObject.increaseP1Score();
                    statObject.addP1Legs(1);
                } else {
                    statObject.increaseP2Score();
                    statObject.addP2Legs(1);
                }
        }

        // Leg statistics
        PersonMatchPartStatManager pmpsm = new PersonMatchPartStatManager(stat);

        fillStatObjectWithLegStats(statObject, pmpsm, personMatchPartKeys, personSport1Id);

        return statObject;
    }

    public StatObject getMatchWithStats(PersonMatchKey pmk) throws SQLException {
        PersonMatch personMatch = new PersonMatchManager(stat).getPersonMatch(pmk);

        return personMatch != null ? getMatchWithStats(pmk, personMatch.getPersonSport1Id()) : null;
    }

    public StatObject getMatchWithStats(PersonMatchKey pmk, int personSport1Id) throws SQLException {
        PersonMatchPartManager pmpm = new PersonMatchPartManager(stat);

        List<PersonMatchPart> sets = pmpm.getPersonMatchPartsWithoutParent(pmk);

        List<PersonMatchPartKey> setKeys = new ArrayList<>();

        StatObject statObject = new StatObject();

        for (PersonMatchPart set : sets) {
            setKeys.add(new PersonMatchPartKey(pmk, set.getPersonMatchPartId()));
            if (set.isFinished())
                if (set.isPerson1Win())
                    statObject.increaseP1Score();
                else
                    statObject.increaseP2Score();
        }

        PersonMatchPartStatManager pmpsm = new PersonMatchPartStatManager(stat);

        List<PersonMatchPartStat> personMatchPartStats =
                pmpsm.getPersonMatchPartStats(setKeys, Collections.singletonList(StatType.statTypeScoreId));

        for (PersonMatchPartStat personMatchPartStat : personMatchPartStats)
            if (personMatchPartStat.getPersonSportId() == personSport1Id)
                statObject.addP1Legs(personMatchPartStat.getValue());
            else
                statObject.addP2Legs(personMatchPartStat.getValue());

        List<PersonMatchPart> legs = pmpm.getPersonMatchPartsFromParents(setKeys);

        List<PersonMatchPartKey> legKeys = new ArrayList<>() {{
            legs.forEach(x -> add(new PersonMatchPartKey(pmk, x.getPersonMatchPartId())));
        }};

        fillStatObjectWithLegStats(statObject, pmpsm, legKeys, personSport1Id);

        return statObject;
    }

    public StandingContext<PersonSport> getPremierLeagueStanding(int seasonId) throws SQLException {
        List<StandingContext<PersonSport>> standingPerDate = getPremierLeagueStandingPerDate(seasonId);

        return !standingPerDate.isEmpty() ? standingPerDate.get(standingPerDate.size() - 1) : null;
    }

    public List<StandingContext<PersonSport>> getPremierLeagueStandingPerDate(int seasonId) throws SQLException {
        CompSeasonKey compSeasonKey = new CompSeasonKey(Competition.competitionIdDartsPremierLeague, seasonId);

        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);

        List<CompSeasonPhase> compSeasonPhases = cspm.getPhasesByCompSeasonAndPhaseTypes(compSeasonKey,
                getPhaseTypeIdsPlayNights());

        List<CompSeasonPhaseKey> compSeasonPhaseKeys = compSeasonPhases.stream().map(CompSeasonPhase::getCompSeasonPhaseKey).toList();
        List<CompSeasonPhaseKey> matchPhaseKeys = cspm.getPhaseListFromParents(compSeasonPhaseKeys)
                .stream().map(CompSeasonPhase::getCompSeasonPhaseKey).toList();

        List<PersonMatch> personMatches = new PersonMatchManager(stat).getMatchesInCompSeasonPhases(matchPhaseKeys);
        personMatches.sort(new H2HMatchDate());

        List<Integer> personSportIds = new CompSeasonPersonSportManager(stat).getParticipantIdsCompSeason(compSeasonKey);
        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(personSportIds);

        Map<Integer, Integer> winsCurrentNight = new HashMap<>();

        personSportIds.forEach(x -> winsCurrentNight.put(x, 0));

        LocalDateTime currentDate = null;
        ParticipantStanding<PersonSport> comparator = new ParticipantStanding<>();

        List<StandingContext<PersonSport>> standings = new ArrayList<>();

        for (PersonMatch personMatch : personMatches) {
            if (personMatch.isFinished() && personMatch.getDate() != null) {
                LocalDateTime date = personMatch.getDate();

                if (currentDate != null && !currentDate.equals(date)) {
                    standings.add(getStandingForDate(personSportMap, winsCurrentNight, currentDate, comparator));
                    personSportIds.forEach(k -> winsCurrentNight.put(k, 0));
                }

                currentDate = date;

                PersonSport ps1 = personSportMap.get(personMatch.getParticipant1Id());
                PersonSport ps2 = personSportMap.get(personMatch.getParticipant2Id());
                PersonSport winner = personSportMap.get(personMatch.getWinnerId());

                ps1.addPlayed(1);
                ps2.addPlayed(1);
                winner.addWin();
                if (winner.getId() == ps1.getId())
                    ps2.addLoss();
                else
                    ps1.addLoss();

                winsCurrentNight.put(winner.getId(), winsCurrentNight.get(winner.getId()) + 1);

                Integer score1 = personMatch.getScore1_1();
                Integer score2 = personMatch.getScore1_2();

                if (score1 != null) {
                    ps1.addScore(score1);
                    ps2.addScoreAgainst(score1);
                }

                if (score2 != null) {
                    ps2.addScore(score2);
                    ps1.addScoreAgainst(score2);
                }

                if (personMatch.getParticipant1NcrId() != null)
                    ps1.addScoreAgainst(6);

                if (personMatch.getParticipant2NcrId() != null)
                    ps2.addScoreAgainst(6);
            }
        }

        if (currentDate != null)
            standings.add(getStandingForDate(personSportMap, winsCurrentNight, currentDate, comparator));

        return standings;
    }

    private void fillStatObjectWithLegStats(StatObject statObject, PersonMatchPartStatManager pmpsm,
                                            List<PersonMatchPartKey> legKeys, int personSport1Id) throws SQLException {
        List<PersonMatchPartStat> legStats = pmpsm.getPersonMatchPartStats(legKeys,
                Arrays.asList(StatType.statTypeMisDubId, StatType.statTypeThrowId));

        for (PersonMatchPartStat personMatchPartStat : legStats) {
            int personSportId = personMatchPartStat.getPersonSportId();
            int statTypeId = personMatchPartStat.getStatTypeId();

            if (personSportId == personSport1Id) {
                if (statTypeId == StatType.statTypeMisDubId)
                    statObject.addP1MisDub(personMatchPartStat.getValue());
                else if (statTypeId == StatType.statTypeThrowId) {
                    int points = personMatchPartStat.getValue();

                    statObject.addP1ThrowTot(points);
                    statObject.addP1ThrowDartTot(personMatchPartStat.getValue2());

                    if (points == 180)
                        statObject.addP1Throw180();
                    else if (points >= 140)
                        statObject.addP1Throw140();
                    else if (points >= 100)
                        statObject.addP1Throw100();
                }
            } else {
                if (statTypeId == StatType.statTypeMisDubId)
                    statObject.addP2MisDub(personMatchPartStat.getValue());
                else if (statTypeId == StatType.statTypeThrowId) {
                    int points = personMatchPartStat.getValue();

                    statObject.addP2ThrowTot(points);
                    statObject.addP2ThrowDartTot(personMatchPartStat.getValue2());

                    if (points == 180)
                        statObject.addP2Throw180();
                    else if (points >= 140)
                        statObject.addP2Throw140();
                    else if (points >= 100)
                        statObject.addP2Throw100();
                }
            }
        }
    }

    private List<Integer> getPhaseTypeIdsPlayNights() throws SQLException {
        return new PhaseTypeManager(stat).getChildIds(PhaseType.phaseTypeIdPlayNight);
    }

    private StandingContext<PersonSport> getStandingForDate(Map<Integer, PersonSport> personSportMap,
                                                            Map<Integer, Integer> winsMap,
                                                            LocalDateTime currentDate,
                                                            ParticipantStanding<PersonSport> comparator) {
        List<PersonSport> standing = new ArrayList<>();

        personSportMap.forEach((k, v) -> {
            PersonSport standingParticipant = new PersonSport();

            int points = 0;

            switch (winsMap.get(k)) {
                case 1: points = 2; break;
                case 2: points = 3; break;
                case 3: points = 5; break;
                default:
            }

            v.addPoints(points);
            v.copyToForStanding(standingParticipant);

            standing.add(standingParticipant);
        });

        Calculation.sortParticipantsAndSetRankBasedFields(standing, comparator);

        return new StandingContext<>(currentDate, standing);
    }
}
