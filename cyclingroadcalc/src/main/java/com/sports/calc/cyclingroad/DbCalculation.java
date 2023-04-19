package com.sports.calc.cyclingroad;

import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonEventPartStage;
import com.sports.entity.comparator.EventPartPersonSportEventDate;
import com.sports.entity.comparator.EventPartPersonSportStage;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record DbCalculation(Statement stat) {
    public boolean setGeneralClassificationPointsForStage(CompSeasonKey compSeasonKey, int stage) throws SQLException {
        List<CompSeasonEventKey> eventKeys = new CompSeasonEventManager(stat).getCompSeasonEventKeys(compSeasonKey);

        CompSeasonEventKey stageRaceKey = null, generalKey = null;

        for (CompSeasonEventKey eventKey : eventKeys) {
            if (Calculation.isStageRace(eventKey.getSportEventId()))
                stageRaceKey = eventKey;
            else if (Calculation.isGeneralClassification(eventKey.getSportEventId()))
                generalKey = eventKey;

            if (stageRaceKey != null && generalKey != null)
                break;
        }

        if (stageRaceKey != null && generalKey != null)
            return setGeneralClassificationPointsForStage(eventKeys, stageRaceKey, generalKey, stage);

        return false;
    }

    public void updateEventPersonSportsWithNoCountResult(CompSeasonEventKey compSeasonEventKey) throws SQLException {
        EventPersonSportManager epsm = new EventPersonSportManager(stat);
        EventPartPersonSportManager eppsm = new EventPartPersonSportManager(stat);

        Map<EventPersonSportKey, EventPersonSport> eventPersonSportMap = epsm.getParticipantMapInEvent(compSeasonEventKey);
        Map<EventPartPersonSportKey, EventPartPersonSport> eventPartsWithNoCountResult = eppsm.getEventPartPersonSportsWithNoCountResult(compSeasonEventKey);

        Map<EventPersonSportKey, EventPersonSport> eventPersonSportsToUpdate = new HashMap<>();

        eventPartsWithNoCountResult.forEach((k, v) -> {
            EventPersonSportKey epsKey = getEventPersonSportKey(k);
            EventPersonSport eps = eventPersonSportMap.get(epsKey);
            if (eps.getNoCountResultId() == null) {
                eps.setNoCountResultId(NoCountResult.noCountResultIdDNF);
                eventPersonSportsToUpdate.put(epsKey, eps);
            }
        });

        epsm.updateParticipantMap(eventPersonSportsToUpdate);
    }

    public List<EventPartPersonSport> getStageWinners(CompSeasonKey csKey) throws SQLException {
        List<EventPartPersonSport> eppsList = new ArrayList<>();

        List<CompSeasonEventKey> cseKeys = new CompSeasonEventManager(stat).getCompSeasonEventKeys(csKey);
        CompSeasonEventKey cseKey = null;

        for (CompSeasonEventKey csek : cseKeys)
            if (Calculation.isStageRace(csek.getSportEventId())) {
                cseKey = csek;
                break;
            }

        if (cseKey != null) {
            eppsList.addAll(new EventPartPersonSportManager(stat).getEventPartPersonSportsWithRankOne(cseKey));

            com.sports.calc.alcifo.DbCalculation alcifoDbCalc = new com.sports.calc.alcifo.DbCalculation(stat);

            alcifoDbCalc.fillCompSeasonEventParts(cseKey, eppsList);
            alcifoDbCalc.fillEventPersonSports(cseKey, eppsList);

            eppsList.sort(new EventPartPersonSportStage());
        }

        return eppsList;
    }

    public List<EventPartPersonSport> getPersonResultsInSeason(int personSportId, int seasonId) throws SQLException {
        Map<CompSeasonKey, CompSeason> compSeasonsMap = getCompSeasonMapForSeason(seasonId);
        List<CompSeasonPersonSportKey> compSeasonPersonSportKeys = compSeasonsMap.keySet().stream().map(x ->
            new CompSeasonPersonSportKey(x, personSportId)).toList();
        List<EventPersonSportKey> personSportKeys = new EventPersonSportManager(stat)
                .getEventsByCompSeasonsForPersonSport(compSeasonPersonSportKeys);

        List<CompSeasonEventKey> compSeasonEventKeys = personSportKeys.stream()
                .map(EventPersonSportKey::getCompSeasonEventKey).toList();
        Map<CompSeasonEventPartKey, CompSeasonEventPart> compSeasonEventPartMap = new CompSeasonEventPartManager(stat)
                .getCompSeasonEventPartMap(compSeasonEventKeys);
        Map<EventPartPersonSportKey, EventPartPersonSport> eventPartPersonSportMap = new EventPartPersonSportManager(stat)
                .getEventPartPersonSportMap(personSportKeys);

        Map<CompSeasonEventPartKey, CompSeasonEventPart> eventPartsForResults = getEventPartsForPersonResults(compSeasonEventPartMap);

        List<EventPartPersonSport> eventPartPersonSports = new ArrayList<>() {{
            eventPartPersonSportMap.forEach((k, v) -> {
                CompSeasonEventPartKey csepKey = k.getSuperKey();
                CompSeasonEventPart compSeasonEventPart = eventPartsForResults.get(csepKey);

                if (compSeasonEventPart != null) {
                    CompSeasonKey compSeasonKey = k.getSuperKey().getSuperKey().getSuperKey();
                    CompSeason compSeason = compSeasonsMap.get(compSeasonKey);

                    compSeasonEventPart.setCompSeasonEndDate(compSeason.getEndDate());
                    v.setCompSeasonEventPart(compSeasonEventPart);

                    add(v);
                }
            });
        }};

        eventPartPersonSports.sort(new EventPartPersonSportEventDate());

        return eventPartPersonSports;
    }

    private EventPersonSportKey getEventPersonSportKey(EventPartPersonSportKey eventPartPersonSportKey) {
        return new EventPersonSportKey(eventPartPersonSportKey.getSuperKey().getSuperKey(),
                eventPartPersonSportKey.getPersonSportId());
    }

    private boolean setGeneralClassificationPointsForStage(List<CompSeasonEventKey> eventKeys,
                                                           CompSeasonEventKey stageRaceKey,
                                                           CompSeasonEventKey generalKey,
                                                           int stage) throws SQLException {
        CompSeasonEventPartManager csepm = new CompSeasonEventPartManager(stat);
        List<CompSeasonEventPart> eventPartsAll = csepm.getCompSeasonEventPartsToStage(eventKeys, stage);

        CompSeasonEventPart eventPartGeneral = getCompSeasonEventPartForStage(eventPartsAll,
                generalKey.getSportEventId(), stage);
        CompSeasonEventPart eventPartGeneralBefore = stage > 1 ? getCompSeasonEventPartForStage(eventPartsAll,
                generalKey.getSportEventId(), stage - 1) : null;
        CompSeasonEventPart eventPartStage = getCompSeasonEventPartForStage(eventPartsAll,
                stageRaceKey.getSportEventId(), stage);

        if (eventPartGeneral != null && eventPartStage != null && (eventPartGeneralBefore != null || stage == 1))
            return setGeneralClassificationPointsForStage(generalKey, stageRaceKey, eventPartGeneral, eventPartStage, eventPartGeneralBefore);

        return false;
    }

    private boolean setGeneralClassificationPointsForStage(CompSeasonEventKey generalKey,
                                                           CompSeasonEventKey stageRaceKey,
                                                           CompSeasonEventPart eventPartGeneral,
                                                           CompSeasonEventPart eventPartStage,
                                                           CompSeasonEventPart eventPartGeneralBefore) throws SQLException {
        CompSeasonEventPartKey generalPartKey =
                new CompSeasonEventPartKey(generalKey, eventPartGeneral.getCompSeasonEventPartId());
        CompSeasonEventPartKey stagePartKey =
                new CompSeasonEventPartKey(stageRaceKey, eventPartStage.getCompSeasonEventPartId());
        CompSeasonEventPartKey generalPartBeforeKey = eventPartGeneralBefore != null ?
                new CompSeasonEventPartKey(generalKey, eventPartGeneralBefore.getCompSeasonEventPartId()) : null;

        List<CompSeasonEventPartKey> partKeyList = new ArrayList<>() {{
            add(generalPartKey);
            add(stagePartKey);
            if (generalPartBeforeKey != null)
                add(generalPartBeforeKey);
        }};

        EventPartPersonSportManager eppsm = new EventPartPersonSportManager(stat);

        Map<EventPartPersonSportKey, EventPartPersonSport> eppsMap = eppsm
                .getEventPartPersonSportMapForEvents(partKeyList);

        Map<EventPartPersonSportKey, EventPartPersonSport> eppsGeneralMap = new HashMap<>();

        eppsMap.forEach((k, v) -> {
            if (k.getSuperKey().equals(generalPartKey)) {
                eppsGeneralMap.put(k, v);

                EventPartPersonSportKey partPersonSportKeyStage =
                        new EventPartPersonSportKey(stagePartKey, k.getPersonSportId());
                EventPartPersonSportKey partPersonSportKeyGeneralBefore = generalPartBeforeKey != null ?
                        new EventPartPersonSportKey(generalPartBeforeKey, k.getPersonSportId()) : null;

                EventPartPersonSport partPersonSportStage = eppsMap.get(partPersonSportKeyStage);
                EventPartPersonSport partPersonSportGeneralBefore = eppsMap.get(partPersonSportKeyGeneralBefore);

                if (partPersonSportStage != null && partPersonSportStage.getNoCountResultId() != null)
                    v.setNoCountResultId(NoCountResult.noCountResultIdDNF);
                else {
                    int points = Util.convertEmptyIntegerToZero(partPersonSportStage != null ? partPersonSportStage.getPoints() : null) +
                            Util.convertEmptyIntegerToZero(partPersonSportGeneralBefore != null ? partPersonSportGeneralBefore.getPoints() : null);

                    v.setPoints(points);
                }
            }
        });

        eppsm.updatePartParticipantMap(eppsGeneralMap);

        return !eppsGeneralMap.isEmpty();
    }

    private CompSeasonEventPart getCompSeasonEventPartForStage(List<CompSeasonEventPart> compSeasonEventParts,
                                                               int sportEventId, int stage) {
        for (CompSeasonEventPart compSeasonEventPart : compSeasonEventParts)
            if (compSeasonEventPart.getSportEventId() == sportEventId && compSeasonEventPart.getStage() == stage)
                return compSeasonEventPart;

        return null;
    }

    private Map<CompSeasonKey, CompSeason> getCompSeasonMapForSeason(int seasonId) throws SQLException {
        List<Competition> competitions = new CompetitionManager(stat).getCompetitionList(Sport.sportIdCyclingRoad);
        return new CompSeasonManager(stat).getCompSeasonMapForSeason(
                competitions.stream().map(Competition::getId).toList(), seasonId);
    }

    private Map<CompSeasonEventPartKey, CompSeasonEventPart> getEventPartsForPersonResults(
            Map<CompSeasonEventPartKey, CompSeasonEventPart> allEventParts) {
        List<CompSeasonEventPart> singleAndStageParts = new ArrayList<>();
        Map<CompSeasonEventKey, List<CompSeasonEventPart>> gcPartMap = new HashMap<>();

        allEventParts.forEach((k, v) -> {
            int sportEventId = v.getSportEventId();

            if (Calculation.isSingleRace(sportEventId) || Calculation.isStageRace(sportEventId))
                singleAndStageParts.add(v);
            else if (Calculation.isGeneralClassification(sportEventId) && v.getStage() != null) {
                CompSeasonEventKey cseKey = k.getSuperKey();
                if (!gcPartMap.containsKey(cseKey))
                    gcPartMap.put(cseKey, new ArrayList<>());

                gcPartMap.get(cseKey).add(v);
            }
        });

        CompSeasonEventPartStage comparator = new CompSeasonEventPartStage();
        gcPartMap.forEach((k, v) -> v.sort(comparator));

        return new HashMap<>() {{
            singleAndStageParts.forEach(x -> put(x.getCompSeasonEventPartKey(), x));
            gcPartMap.forEach((k, v) -> {
                CompSeasonEventPart csePart = v.get(v.size() - 1);
                put(csePart.getCompSeasonEventPartKey(), csePart);
            });
        }};
    }
}
