package com.sports.calc.cyclingroad;

import com.sports.calc.alcifo.EventPersonSportRankFromFinalEventPartUpdater;
import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonEventPartStage;
import com.sports.entity.comparator.EventPartPersonSportEventDate;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public record DbCalculation(Statement stat) {
    public boolean setGeneralClassificationPointsForStage(CompSeasonKey compSeasonKey, int stage) throws SQLException {
        List<CompSeasonEvent> compSeasonEvents = new CompSeasonEventManager(stat).getCompSeasonEvents(compSeasonKey);

        CompSeasonEvent stageRace = null, general = null;

        for (CompSeasonEvent compSeasonEvent : compSeasonEvents) {
            int sportEventId = compSeasonEvent.getSportEventKey().getSportEventId();

            if (sportEventId == SportEvent.sportEventIdCyclingRoadStage)
                stageRace = compSeasonEvent;
            else if (sportEventId == SportEvent.sportEventIdCyclingRoadGeneral)
                general = compSeasonEvent;

            if (stageRace != null && general != null)
                break;
        }

        if (stageRace != null && general != null)
            return setGeneralClassificationPointsForStage(compSeasonKey, compSeasonEvents, stageRace, general, stage);

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

    public void updateEventPersonSportsFromFinalEventPart(CompSeasonEventKey cseKey) throws SQLException {
        new EventPersonSportRankFromFinalEventPartUpdater<EventPartPersonSport>(cseKey, stat) {
            @Override
            public Map<EventPersonSportKey, EventPartPersonSport> getSourceMap(CompSeasonEventPartKey csepKey, Statement stat) throws SQLException {
                return new HashMap<>() {{
                    new EventPartPersonSportManager(stat).getPartParticipantMap(Collections.singletonList(csepKey)).forEach((k, v) ->
                        put(getEventPersonSportKey(k), v));
                }};
            }

            @Override
            public Integer getRank(EventPartPersonSport entity) {
                return entity.getRank();
            }
        }.updateEventPersonSportRanks();
    }

    public List<EventPartPersonSport> getPersonResultsInSeason(int personSportId, int seasonId) throws SQLException {
        Map<CompSeasonKey, CompSeason> compSeasonsMap = getCompSeasonMapForSeason(seasonId);
        List<CompSeasonPersonSportKey> compSeasonPersonSportKeys = compSeasonsMap.keySet().stream().map(x ->
            new CompSeasonPersonSportKey(x, personSportId)).toList();
        List<EventPersonSportKey> personSportKeys = new EventPersonSportManager(stat)
                .getEventsByCompSeasonsForPersonSport(compSeasonPersonSportKeys);

        List<CompSeasonEventKey> compSeasonEventKeys = personSportKeys.stream()
                .map(EventPersonSportKey::getCompSeasonEventKey).toList();
        Map<CompSeasonEventKey, CompSeasonEvent> compSeasonEventMap = new CompSeasonEventManager(stat)
                .getCompSeasonEventMap(compSeasonEventKeys);
        Map<CompSeasonEventPartKey, CompSeasonEventPart> compSeasonEventPartMap = new CompSeasonEventPartManager(stat)
                .getCompSeasonEventPartMap(compSeasonEventKeys);
        Map<EventPartPersonSportKey, EventPartPersonSport> eventPartPersonSportMap = new EventPartPersonSportManager(stat)
                .getEventPartPersonSportMap(personSportKeys);

        Map<CompSeasonEventPartKey, CompSeasonEventPart> eventPartsForResults =
                getEventPartsForPersonResults(compSeasonEventMap, compSeasonEventPartMap);

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

    private boolean setGeneralClassificationPointsForStage(CompSeasonKey compSeasonKey,
                                                           List<CompSeasonEvent> compSeasonEvents,
                                                           CompSeasonEvent stageRace,
                                                           CompSeasonEvent general,
                                                           int stage) throws SQLException {
        CompSeasonEventPartManager csepm = new CompSeasonEventPartManager(stat);

        Map<CompSeasonEventKey, CompSeasonEvent> compSeasonEventMap = new HashMap<>() {{
            compSeasonEvents.forEach(x -> {
                CompSeasonEventKey cseKey = new CompSeasonEventKey(compSeasonKey, x.getCompSeasonEventId());
                put(cseKey, x);
            });
        }};
        List<CompSeasonEventPart> eventPartsAll = csepm.getCompSeasonEventPartsToStage(
                compSeasonEventMap.keySet().stream().toList(), stage);

        CompSeasonEventPart eventPartGeneral = getCompSeasonEventPartForStage(eventPartsAll,
                compSeasonEventMap, general.getSportEventKey().getSportEventId(), stage);
        CompSeasonEventPart eventPartGeneralBefore = stage > 1 ? getCompSeasonEventPartForStage(eventPartsAll,
                compSeasonEventMap, general.getSportEventKey().getSportEventId(), stage - 1) : null;
        CompSeasonEventPart eventPartStage = getCompSeasonEventPartForStage(eventPartsAll,
                compSeasonEventMap, stageRace.getSportEventKey().getSportEventId(), stage);

        if (eventPartGeneral != null && eventPartStage != null && (eventPartGeneralBefore != null || stage == 1)) {
            CompSeasonEventKey generalKey = new CompSeasonEventKey(compSeasonKey, general.getCompSeasonEventId());
            CompSeasonEventKey stageRaceKey = new CompSeasonEventKey(compSeasonKey, stageRace.getCompSeasonEventId());

            return setGeneralClassificationPointsForStage(generalKey, stageRaceKey, eventPartGeneral, eventPartStage,
                    eventPartGeneralBefore);
        }

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
                                                               Map<CompSeasonEventKey, CompSeasonEvent> compSeasonEventMap,
                                                               int sportEventId, int stage) {
        for (CompSeasonEventPart compSeasonEventPart : compSeasonEventParts) {
            int sportEventIdEventPart = getSportEventId(compSeasonEventPart, compSeasonEventMap);

            if (sportEventIdEventPart == sportEventId && compSeasonEventPart.getStage() == stage)
                return compSeasonEventPart;
        }

        return null;
    }

    private Map<CompSeasonKey, CompSeason> getCompSeasonMapForSeason(int seasonId) throws SQLException {
        List<Competition> competitions = new CompetitionManager(stat).getCompetitionList(Sport.sportIdCyclingRoad);
        return new CompSeasonManager(stat).getCompSeasonMapForSeason(
                competitions.stream().map(Competition::getId).toList(), seasonId);
    }

    private Map<CompSeasonEventPartKey, CompSeasonEventPart> getEventPartsForPersonResults(
            Map<CompSeasonEventKey, CompSeasonEvent> compSeasonEventMap,
            Map<CompSeasonEventPartKey, CompSeasonEventPart> allEventParts) {
        List<CompSeasonEventPart> singleAndStageParts = new ArrayList<>();
        Map<CompSeasonEventKey, List<CompSeasonEventPart>> gcPartMap = new HashMap<>();

        allEventParts.forEach((k, v) -> {
            int sportEventId = getSportEventId(v, compSeasonEventMap);

            if (sportEventId == SportEvent.sportEventIdCyclingRoadSingle || sportEventId == SportEvent.sportEventIdCyclingRoadStage)
                singleAndStageParts.add(v);
            else if (sportEventId == SportEvent.sportEventIdCyclingRoadGeneral && v.getStage() != null) {
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

    private int getSportEventId(CompSeasonEventPart csep, Map<CompSeasonEventKey, CompSeasonEvent> cseMap) {
        CompSeasonEventKey cseKey = csep.getCompSeasonEventPartKey().getSuperKey();
        return cseMap.get(cseKey).getSportEventKey().getSportEventId();
    }
}
