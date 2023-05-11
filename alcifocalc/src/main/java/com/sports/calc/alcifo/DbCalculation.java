package com.sports.calc.alcifo;

import com.sports.entity.*;
import com.sports.entity.comparator.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public record DbCalculation(Statement stat) {
    public void fillDisciplinePartsForEventDisciplineParts(SportDisciplineKey sportDisciplineKey,
                                                           List<EventDisciplinePart> eventDisciplineParts)
            throws SQLException {
        List<DisciplinePart> disciplineParts = new DisciplinePartManager(stat).getDisciplinePartList(sportDisciplineKey);

        for (EventDisciplinePart eventDisciplinePart : eventDisciplineParts)
            for (DisciplinePart disciplinePart : disciplineParts)
                if (eventDisciplinePart.getDisciplinePartId() == disciplinePart.getDisciplinePartId()) {
                    eventDisciplinePart.setDisciplinePart(disciplinePart);
                    break;
                }
    }

    public void postMortemInsertCompSeasonEvent(CompSeasonEventKey csek, CompSeasonEvent cse) throws SQLException {
        SportEventPartManager sepm = new SportEventPartManager(stat);
        DisciplinePartManager dppm = new DisciplinePartManager(stat);

        SportEventKey sek = cse.getSportEventKey();

        List<SportEventPart> sportEventParts = sepm.getSportEventParts(cse.getSportEventKey());

        Map<CompSeasonEventPartKey, CompSeasonEventPart> csepMap = new HashMap<>();
        List<SportDisciplineKey> sportDisciplineKeys = new ArrayList<>();

        for (SportEventPart sportEventPart : sportEventParts) {
            sportDisciplineKeys.add(new SportDisciplineKey(sek.getSportId(),
                    sportEventPart.getSportDisciplineId()));

            CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(csek, sportEventPart.getSportEventPartId());
            CompSeasonEventPart csep = new CompSeasonEventPart();
            csep.setSportId(sek.getSportId());
            csep.setSportDisciplineId(sportEventPart.getSportDisciplineId());
            csep.setOrder(sportEventPart.getOrder());

            csepMap.put(csepKey, csep);
        }

        List<DisciplinePart> disciplineParts = dppm.getDisciplinePartList(sportDisciplineKeys);
        disciplineParts.sort(new DisciplinePartSportDisciplineId());

        Map<EventDisciplinePartKey, EventDisciplinePart> edpMap = new HashMap<>();

        for (Map.Entry<CompSeasonEventPartKey, CompSeasonEventPart> me : csepMap.entrySet()) {
            int sportDisciplineId = me.getValue().getSportDisciplineId();
            List<DisciplinePart> selectedDisciplineParts =
                    getDisciplinePartsFromSportDiscipline(disciplineParts, sportDisciplineId);

            for (DisciplinePart disciplinePart : selectedDisciplineParts) {
                EventDisciplinePartKey edpk = new EventDisciplinePartKey(me.getKey(), disciplinePart.getDisciplinePartId());

                EventDisciplinePart edp = new EventDisciplinePart();
                edp.setSportId(sek.getSportId());
                edp.setSportDisciplineId(sportDisciplineId);
                edp.setDisciplinePartId(disciplinePart.getDisciplinePartId());
                edp.setName(disciplinePart.getName());

                edpMap.put(edpk, edp);
            }
        }

        new CompSeasonEventPartManager(stat).insertCompSeasonEventParts(csepMap);
        new EventDisciplinePartManager(stat).insertEventDisciplineParts(edpMap);
    }

    public boolean hasSportEventParts(SportEventKey sek) throws SQLException {
        return getSportEventHasFixedPartsMap(Collections.singletonList(sek)).get(sek);
    }

    public boolean hasDisciplineParts(CompSeasonEventPartKey csepk) throws SQLException {
        CompSeasonEventPart csep = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepk);
        List<DisciplinePart> disciplineParts = new DisciplinePartManager(stat)
                .getDisciplinePartList(Collections.singletonList(csep.getSportDisciplineKey()));

        return disciplineParts.size() > 0;
    }

    public void insertParticipantsFromCompSeason(CompSeasonEventKey cseKey, CompSeasonEvent cse) throws SQLException {
        CompSeasonKey csKey = cseKey.getSuperKey();
        SportEvent sportEvent = new SportEventManager(stat).getEntityFromSuperKey(cse.getSportEventKey());

        if (sportEvent != null) {
            AlcifoParticipantFactory factory = Calculation.getAlcifoParticipantFactory(sportEvent);

            List<Integer> personSportIds = factory.getCompSeasonParticipantManager(stat).getParticipantIdsCompSeason(csKey);
            Map partMap = new HashMap<SuperKey, AlcifoParticipant>() {{
                personSportIds.forEach(id -> put(factory.getAlcifoParticipantKey(cseKey, id), factory.getAlcifoParticipant()));
            }};

            factory.getManager(stat).insertParticipantMap(partMap);
        }
    }

    public Map<SportEventKey, Boolean> getSportEventHasFixedPartsMap(List<SportEventKey> seKeys) throws SQLException {
        Map<SportEventKey, Boolean> resultMap = new HashMap<>() {{
            seKeys.forEach(x -> put(x, false));
        }};

        List<SportEventPartKey> sepKeys = new SportEventPartManager(stat).getSportEventPartKeys(seKeys);
        sepKeys.forEach(sepKey -> resultMap.put(sepKey.getSuperKey(), true));

        return resultMap;
    }

    public void insertPartParticipants(SuperKey partKey, AlcifoPartParticipantFactory factory) throws SQLException {
        AlcifoPartParticipantManager partParticipantManager = factory.getManager(stat);
        AlcifoParticipantManager participantManager = factory.getParticipantFactory().getManager(stat);

        CompSeasonEventKey cseKey = factory.getCompSeasonEventPartKey(partKey).getSuperKey();

        Map partParticipantMap = new HashMap<SuperKey, AlcifoPartParticipant>() {{
            Map<? extends AlcifoParticipantKey, ? extends AlcifoParticipant> pPartMap =
                    participantManager.getParticipantMapInEvent(cseKey);
            pPartMap.forEach((k, v) -> {
                if (v.getNoCountResultId() == null)
                    put(factory.getKey(partKey, k.getParticipantId()), factory.getInstance());
            });
        }};

        partParticipantManager.insertPartParticipantMap(partParticipantMap);
    }

    public void updatePartParticipants(SuperKey partKey, List<? extends AlcifoPartParticipant> partParticipants,
                                       AlcifoPartParticipantFactory factory) throws SQLException {
        Map updateMap = new HashMap() {{
            partParticipants.forEach(x -> {
                SuperKey key = factory.getKey(partKey, x.getParticipantId());
                put(key, x);
            });
        }};

        factory.getManager(stat).updatePartParticipantMap(updateMap);
    }

    public <U extends SuperKey> List<? extends Participant> getFullRankingInPart(AlcifoPartParticipantFactory factory, U partKey)
            throws SQLException {
        AlcifoPartParticipantManager<?, U, ?> partParticipantManager = (AlcifoPartParticipantManager<?, U, ?>) factory.getManager(stat);

        CompSeasonEventKey cseKey = factory.getCompSeasonEventPartKey(partKey).getSuperKey();
        CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);

        List<? extends AlcifoPartParticipant> pPartList = partParticipantManager.getPartParticipantList(partKey);
        Map<SuperKey, AlcifoPartParticipant> pPartMap = new HashMap<>() {{
            pPartList.forEach(x -> {
                SuperKey key = factory.getKey(partKey, x.getParticipantId());
                put(key, x);
            });
        }};

        SportEvent se = new SportEventManager(stat).getEntityFromSuperKey(cse.getSportEventKey());
        if (se != null)
            sortAndRankPartParticipantsByPoints(pPartList, se.isPointsSortAsc());
        pPartList.forEach(AlcifoPartParticipant::overrideNullRankWithCalculatedRank);

        List<? extends Participant> participants = factory.getParticipantManager(stat).getParticipantList(
                pPartList.stream().map(AlcifoPartParticipant::getParticipantId).collect(Collectors.toList())
        );

        setRanksAndPoints(factory, partKey, participants, pPartMap);
        participants.sort(new ParticipantRank());

        if (participants.size() > 0 && participants.get(0).getPoints() != null && se != null)
            setPointsBehind(participants, participants.get(0).getPoints(), se.isPointsSortAsc());

        return participants;
    }

    /**
     * Sets the descriptions for the parts in a given competition season event.
     * The order of preference is
     * - Name of sport event part
     * - Name of event part name property
     * - Name of the defined geo in the single event part location linked to it
     * - Name of the sport discipline
     *
     * @param compSeasonEventKey   The key of the competition season event
     * @param compSeasonEventParts The list of event parts
     * @throws SQLException if a database error occurs
     */
    public void setCompSeasonEventPartDescriptions(CompSeasonEventKey compSeasonEventKey,
                                                   List<CompSeasonEventPart> compSeasonEventParts) throws SQLException {
        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(compSeasonEventKey);
        List<SportEventPart> sportEventParts = new SportEventPartManager(stat).getSportEventParts(
                compSeasonEvent.getSportEventKey());

        if (sportEventParts.size() > 0)
            setEventPartDescriptionsFromFixedParts(compSeasonEventParts, sportEventParts);
        else
            setEventPartDescriptionsWithoutFixedParts(compSeasonEventKey, compSeasonEventParts);
    }

    public void setEventPartLocationStringFields(List<EventPartLocation> eventPartLocations) throws SQLException {
        List<Integer> geoIds = new ArrayList<>();

        eventPartLocations.forEach(x -> {
            if (x.getGeoId() != null)
                geoIds.add(x.getGeoId());
        });

        Map<Integer, Geo> geoMap = new GeoManager(stat).getGeoMap(geoIds);
        List<LocationRole> locationRoles = new LocationRoleManager(stat).getAllLocationRoles(); // Full retrieval and
        // linear search should be fast enough.

        eventPartLocations.forEach(x -> {
            String roleName = null;
            String description;

            for (LocationRole locationRole : locationRoles)
                if (locationRole.getId() == x.getLocationRoleId()) {
                    roleName = locationRole.getName();
                    break;
                }

            description = x.getGeoId() != null ? geoMap.get(x.getGeoId()).getName() : roleName;

            x.setRoleName(roleName);
            x.setDescription(description);
        });
    }

    public boolean isAlcifo(int competitionId) throws SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);
        Sport sport = competition != null ? new SportManager(stat).getSport(competition.getSportId()) : null;

        return sport != null && !sport.isTeam() && !sport.isH2H();
    }

    public SportEvent getSportEvent(CompSeasonEventKey compSeasonEventKey) throws SQLException {
        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(compSeasonEventKey);
        return new SportEventManager(stat).getEntityFromSuperKey(compSeasonEvent.getSportEventKey());
    }

    private List<DisciplinePart> getDisciplinePartsFromSportDiscipline(List<DisciplinePart> disciplineParts,
                                                                       int sportDisciplineId) {
        List<DisciplinePart> selectedDisciplineParts = new ArrayList<>();

        int indX = 0;

        while (indX < disciplineParts.size() && disciplineParts.get(indX).getSportDisciplineId() != sportDisciplineId)
            indX++;

        while (indX < disciplineParts.size() && disciplineParts.get(indX).getSportDisciplineId() == sportDisciplineId)
            selectedDisciplineParts.add(disciplineParts.get(indX++));

        return selectedDisciplineParts;
    }

    private void setRanksAndPoints(AlcifoPartParticipantFactory factory,
                                   SuperKey partKey,
                                   List<? extends Participant> participants,
                                   Map<? extends SuperKey, ? extends AlcifoPartParticipant> partParticipantMap) {
        participants.forEach(pt -> {
            AlcifoPartParticipant pPart = partParticipantMap.get(factory.getKey(partKey, pt.getId()));
            Integer rank = pPart != null ? pPart.getRank() : null;
            Integer points = pPart != null ? pPart.getPoints() : null;
            Integer noCountResultId = pPart != null ? pPart.getNoCountResultId() : null;

            pt.setRank(rank);
            pt.setPoints(points);
            pt.setNoCountResultId(noCountResultId);
        });
    }

    private Map<CompSeasonEventPartKey, List<EventPartLocation>> getEventPartLocationMap(CompSeasonEventKey cseKey)
            throws SQLException {
        Map<CompSeasonEventPartKey, List<EventPartLocation>> map = new HashMap<>();

        List<EventPartLocation> eventPartLocations =
                new EventPartLocationManager(stat).getEventPartLocations(cseKey);

        eventPartLocations.forEach(x -> {
            CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(cseKey, x.getCompSeasonEventPartId());
            if (!map.containsKey(csepKey))
                map.put(csepKey, new ArrayList<>());

            map.get(csepKey).add(x);
        });

        return map;
    }

    private Map<CompSeasonEventPartKey, Geo> getGeoMapFromEventPartLocations(Map<CompSeasonEventPartKey, List<EventPartLocation>>
                                                                                     eventPartLocationMap) throws SQLException {
        Map<CompSeasonEventPartKey, Geo> map = new HashMap<>();
        Map<Integer, List<CompSeasonEventPartKey>> inverseMap = new HashMap<>();

        eventPartLocationMap.forEach((k, v) -> {
            if (v.size() == 1 && v.get(0).getGeoId() != null) {
                int geoId = v.get(0).getGeoId();
                if (!inverseMap.containsKey(geoId))
                    inverseMap.put(geoId, new ArrayList<>());

                inverseMap.get(geoId).add(k);
            }
        });

        List<Integer> geoIds = new ArrayList<>(inverseMap.keySet());
        Map<Integer, Geo> geoMap = new GeoManager(stat).getGeoMap(geoIds);
        geoMap.forEach((k, v) -> inverseMap.get(k).forEach(x -> map.put(x, v)));

        return map;
    }

    private void sortAndRankPartParticipantsByPoints(List<? extends AlcifoPartParticipant> partParticipants,
                                                     boolean sortAscending) {
        Comparator<AlcifoPartParticipant> comparator =
                sortAscending ? new AlcifoPartParticipantPoints() : new AlcifoPartParticipantPointsDesc();

        partParticipants.sort(comparator);

        int curRank = 0;

        for (int i = 0; i < partParticipants.size() && partParticipants.get(i).getPoints() != null; i++) {
            AlcifoPartParticipant prevPartParticipant = i > 0 ? partParticipants.get(i - 1) : null;
            AlcifoPartParticipant partParticipant = partParticipants.get(i);

            if (prevPartParticipant == null || comparator.compare(partParticipant, prevPartParticipant) > 0)
                curRank = i + 1;

            partParticipant.setCalculatedRank(curRank);
        }
    }

    private void setPointsBehind(List<? extends Participant> participants, int pointsLeader, boolean sortAscending) {
        participants.forEach(p -> {
            Integer points = p.getPoints();

            if (points != null) {
                int pointsBehind = sortAscending ? points - pointsLeader : pointsLeader - points;
                p.setPointsBehind(pointsBehind);
            }
        });
    }

    private void setEventPartDescriptionsFromFixedParts(List<CompSeasonEventPart> compSeasonEventParts,
                                                        List<SportEventPart> sportEventParts) {
        Comparator<Orderable> comparator = new OrderableOrder();
        compSeasonEventParts.sort(comparator);
        sportEventParts.sort(comparator);

        for (int i = 0; i < Math.min(compSeasonEventParts.size(), sportEventParts.size()); i++) {
            CompSeasonEventPart compSeasonEventPart = compSeasonEventParts.get(i);
            SportEventPart sportEventPart = sportEventParts.get(i);

            compSeasonEventPart.setDescription(sportEventPart.getName());
        }
    }

    private void setEventPartDescriptionsWithoutFixedParts(CompSeasonEventKey compSeasonEventKey,
                                                           List<CompSeasonEventPart> compSeasonEventParts)
        throws SQLException {
        List<SportDisciplineKey> sdKeys = new ArrayList<>();
        List<Integer> epnIds = new ArrayList<>();

        compSeasonEventParts.forEach(x -> {
            if (x.getEventPartNameId() != null)
                epnIds.add(x.getEventPartNameId());
            else
                sdKeys.add(x.getSportDisciplineKey());
        });

        Map<Integer, EventPartName> epnMap = new EventPartNameManager(stat).getEventPartNameMap(epnIds);

        boolean searchEventPartLocations = compSeasonEventParts.stream().anyMatch(x -> x.getEventPartNameId() == null);
        Map<CompSeasonEventPartKey, List<EventPartLocation>> eplMap =
                (searchEventPartLocations ? getEventPartLocationMap(compSeasonEventKey) : new HashMap<>());
        Map<CompSeasonEventPartKey, Geo> geoMap = getGeoMapFromEventPartLocations(eplMap);

        Map<SportDisciplineKey, SportDiscipline> sdMap = new SportDisciplineManager(stat).getSportDisciplineMap(sdKeys);

        compSeasonEventParts.forEach(x -> {
            String name;

            CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(compSeasonEventKey, x.getCompSeasonEventPartId());

            if (x.getEventPartNameId() != null)
                name = epnMap.get(x.getEventPartNameId()).getName();
            else if (geoMap.containsKey(csepKey))
                name = geoMap.get(csepKey).getName();
            else
                name = sdMap.get(x.getSportDisciplineKey()).getName();

            x.setDescription(name);
        });
    }
}
