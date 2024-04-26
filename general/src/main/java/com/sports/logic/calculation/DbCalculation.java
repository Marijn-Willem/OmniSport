package com.sports.logic.calculation;

import com.sports.entity.Double;
import com.sports.entity.*;
import com.sports.entity.comparator.OrderableOrder;
import com.sports.entity.comparator.PersonSportSportId;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.factory.CompSeasonDoubleFactory;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.sports.logic.calculation.Calculation.*;

public record DbCalculation(Statement stat) {
    public CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getCompSeasonParticipantFactory(Competition competition)
        throws SQLException {
        if (competition.isH2hDouble())
            return new CompSeasonDoubleFactory();

        Sport sport = new SportManager(stat).getTeamSportsList(Collections.singletonList(competition.getSportId())).get(0);

        return Calculation.getCompSeasonParticipantFactory(competition, sport);
    }

    public CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getCompSeasonParticipantFactory(int competitionId) throws SQLException {
        return getCompSeasonParticipantFactory(new CompetitionManager(stat).getCompetition(competitionId));
    }

    public boolean copyCompSeasonToSeason(CompSeasonKey csk, int seasonId) throws SQLException {
        CompSeasonKey newCsk = new CompSeasonKey(csk.getCompetitionId(), seasonId);

        CompSeasonManager csm = new CompSeasonManager(stat);

        CompSeason cm = csm.getCompSeason(newCsk);

        if (cm == null) {
            cm = csm.getCompSeason(csk);

            if (cm != null) {
                cm.setStartDate(null);
                cm.setEndDate(null);

                csm.insertCompSeason(newCsk, cm);
                copyCompSeasonPhases(csk, seasonId);
                copySportsEvents(csk, seasonId);

                return true;
            }
        }

        return false;
    }

    public Map<String, Person> getPersonNameMapWithNewPersons(List<String> names,
                                                              int competitionId) throws SQLException {
        Map<String, Person> personNameMap = new HashMap<>();

        Competition comp = new CompetitionManager(stat).getCompetition(competitionId);

        if (comp != null)
            personNameMap = getPersonNameMapWithNewPersons(names, comp);

        return personNameMap;
    }

    public Map<String, Person> getPersonNameMapWithNewPersons(List<String> names, CompSeasonEvent cse) throws SQLException {
        return getPersonNameMapWithNewPersonsForGender(names, cse.getGenderId());
    }

    public Map<String, Double> getDoubleMapWithNewDoubles(List<String[]> namePairs, int competitionId) throws SQLException {
        List<String> personNames = new ArrayList<>();

        for (String[] namePair : namePairs) {
            personNames.add(namePair[0]);
            personNames.add(namePair[1]);
        }

        Map<String, Person> personNameMap = getPersonNameMapWithNewPersons(personNames, competitionId);
        Map<String, PersonSport> descrPersonSportMap = getDescrPersonSportMap(new ArrayList<>(personNameMap.values()), competitionId);

        Map<Integer, Person> personMap = new HashMap<>() {{
            personNameMap.forEach((k, v) -> put(v.getId(), v));
        }};

        Map<Integer, PersonSport> personSportMap = new HashMap<>();
        Map<Integer, Integer> personIdPersonSportIdMap = new HashMap<>();

        descrPersonSportMap.forEach((k, v) -> {
            personSportMap.put(v.getId(), v);
            personIdPersonSportIdMap.put(v.getPersonId(), v.getId());
        });

        PersonManager pm = new PersonManager(stat);

        for (int i = 0; i < personNames.size(); i += 2) {
            Person p1 = personNameMap.get(personNames.get(i));
            Person p2 = personNameMap.get(personNames.get(i + 1));

            if (p1.isNewlyCreated())
                updatePersonFromDoublePartner(pm, p1, p2);

            if (p2.isNewlyCreated())
                updatePersonFromDoublePartner(pm, p2, p1);
        }

        Calculation.setGenderIdsOnPersonSports(personSportMap.values(), personMap);

        List<DoublePersonSport1PersonSport2Key> keys = new ArrayList<>();

        for (int i = 0; i < personNames.size(); i += 2) {
            Person p1 = personNameMap.get(personNames.get(i));
            Person p2 = personNameMap.get(personNames.get(i + 1));

            int personSport1Id = personIdPersonSportIdMap.get(p1.getId());
            int personSport2Id = personIdPersonSportIdMap.get(p2.getId());

            keys.add(getDoubleKey(personSport1Id, personSport2Id, personSportMap));
        }

        return getDoubleNameMapFromKeys(keys, personSportMap, personMap);
    }

    public List<Double> getDoubleListWithNewDoubles(List<String[]> namePairs, int competitionId)
            throws SQLException {
        List<Double> doubles = new ArrayList<>();

        Map<String, Double> nameDblMap = getDoubleMapWithNewDoubles(namePairs, competitionId);
        for (Map.Entry<String, Double> me : nameDblMap.entrySet())
            doubles.add(me.getValue());

        return doubles;
    }

    public List<? extends Participant> getParticipantStandingCompSeasonPhase(CompSeasonPhaseKey cspk)
            throws SQLException {
        return getCompSeasonParticipantFactory(cspk.getCompetitionId()).getH2HObjectFactory()
                .getStandingProcessor(stat, cspk).getStanding();
    }

    public void dedoublePersons(int personFromId, int personToId) throws SQLException {
        dedoublePersonSports(personFromId, personToId);

        new PersonManager(stat).deletePerson(personFromId);
    }

    public void splitPerson(Person person) throws SQLException {
        PersonManager pm = new PersonManager(stat);
        PersonSportManager psm = new PersonSportManager(stat);

        List<PersonSport> personSports = psm.getPersonSportsForPerson(person.getId());
        List<Sport> sportList = new SportManager(stat).getFullSportList();
        List<Person> personsToAdd = new ArrayList<>();
        int startId = pm.getNewPersonId();
        String baseName = person.getName();

        Sport sport;
        String name;

        assert !personSports.isEmpty();
        sport = getSport(sportList, personSports.get(0).getSportId());
        assert sport != null;
        name = baseName + " (" + sport.getName() + ")";
        person.setName(name);
        personSports.get(0).setDescription(name);

        for (int i = 1; i < personSports.size(); i++) {
            PersonSport personSport = personSports.get(i);
            sport = getSport(sportList, personSport.getSportId());
            assert sport != null;

            name = baseName + " (" + sport.getName() + ")";
            Person personToAdd = person.getCopy();
            personToAdd.setName(name);
            personsToAdd.add(personToAdd);

            personSport.setPersonId(startId + i - 1);
            personSport.setDescription(name);
        }

        Map<Integer, PersonSport> personSportMap = new HashMap<>() {{
            personSports.forEach(x -> put(x.getId(), x));
        }};

        pm.update(person.getId(), person);
        pm.insertPersons(personsToAdd, startId);
        psm.updatePersonSports(personSportMap);
    }

    public void setGeoOutputStrings(List<Geo> geos) throws SQLException {
        GeoManager gm = new GeoManager(stat);
        GeoTypeManager gtm = new GeoTypeManager(stat);

        List<Integer> parentGeoIds = new ArrayList<>();
        for (Geo geo : geos)
            if (geo.getParentGeoId() != null)
                parentGeoIds.add(geo.getParentGeoId());

        Map<Integer, Geo> parentGeoMap = gm.getGeoMap(parentGeoIds);
        Map<Integer, GeoType> geoTypeMap = gtm.getAllGeoTypesAsMap();

        for (Geo geo : geos)
            geo.setOutputString(getGeoOutputString(geo, geoTypeMap, parentGeoMap));
    }

    public Geo getGeoFromOutputString(String outputString) throws SQLException {
        GeoManager gm = new GeoManager(stat);
        GeoTypeManager gtm = new GeoTypeManager(stat);

        Geo result = null;

        String[] parts = outputString.split("\\s\\|\\s");
        if (parts.length == 2 || parts.length == 3) {
            GeoType geoType = gtm.getGeoTypeByName(parts[1]);

            if (geoType != null) {
                String name = parts[0];

                List<Geo> parentGeos = parts.length == 3 ? gm.getGeosFromName(parts[2]) : new ArrayList<>();
                List<Integer> parentGeoIds = new ArrayList<>() {{
                    addAll(parentGeos.stream().map(Geo::getId).toList());
                }};

                List<Geo> geoList = gm.getGeosByUniqueFields(name, geoType.getId(), parentGeoIds);
                if (geoList.size() == 1)
                    result = geoList.get(0);
            }
        }

        return result;
    }

    public CompSeasonKey getPreviousCompSeason(CompSeasonKey compSeasonKey) throws SQLException {
        CompSeasonManager csm = new CompSeasonManager(stat);
        SeasonManager sm = new SeasonManager(stat);

        int competitionId = compSeasonKey.getCompetitionId();
        List<Integer> seasonIds = csm.getSeasonIdsForCompetition(competitionId);
        List<Season> seasons = sm.getSeasonList(seasonIds);

        seasons.sort(new OrderableOrder());

        for (int i = 1; i < seasons.size(); i++)
            if (seasons.get(i).getId() == compSeasonKey.getSeasonId())
                return new CompSeasonKey(competitionId, seasons.get(i - 1).getId());

        return null;
    }

    public List<PersonSport> getPersonSportsWithNewInstances(int sportId, List<Person> persons) throws SQLException {
        List<PersonSportIdKey> keyList = new ArrayList<>();
        Map<Integer, Person> personMap = new HashMap<>();

        for (Person person : persons) {
            keyList.add(new PersonSportIdKey(person.getId(), sportId));
            personMap.put(person.getId(), person);
        }

        PersonSportManager psm = new PersonSportManager(stat);

        List<PersonSport> personSports = psm.getPersonSportsFromPersonSportIds(keyList);

        Set<PersonSportIdKey> existingKeys = new HashSet<>();
        for (PersonSport personSport : personSports)
            existingKeys.add(personSport.getPersonSportIdKey());

        List<PersonSport> personSportsToCreate = new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            PersonSportIdKey key = keyList.get(i);

            if (!existingKeys.contains(key)) {
                PersonSport personSport = new PersonSport();
                personSport.setPersonId(person.getId());
                personSport.setSportId(sportId);
                personSport.setDescription(person.getName());
                personSportsToCreate.add(personSport);
            }
        }

        if (!personSportsToCreate.isEmpty()) {
            insertNewPersonSports(psm, personSportsToCreate);
            personSports.addAll(personSportsToCreate);
        }

        Calculation.setGenderIdsOnPersonSports(personSports, personMap);

        return personSports;
    }

    public int getSportId(int competitionId) throws SQLException {
        return new CompetitionManager(stat).getCompetition(competitionId).getSportId();
    }

    public List<Language> getReferencingLanguages(int languageId) throws SQLException {
        List<Language> referencingLanguages = new ArrayList<>();

        LanguageManager lm = new LanguageManager(stat);

        Queue<Language> references = new LinkedList<>(lm.getDirectFallbackLanguages(languageId));

        while (!references.isEmpty()) {
            Language language = references.poll();
            references.addAll(lm.getDirectFallbackLanguages(language.getId()));
            referencingLanguages.add(language);
        }

        return referencingLanguages;
    }

    public Alias getAliasForClient(AliasEntityIdKey key, int clientId) throws SQLException {
        List<Alias> aliases = new AliasManager(stat).getAliasListFromEntityId(key);

        for (Alias alias : aliases)
            if (Util.compareIntegers(alias.getClientId(), clientId))
                return alias;

        Client client = new ClientManager(stat).getEntityFromId(clientId);
        List<Integer> languageIds = new ArrayList<>();

        if (client.getLanguageId() != null) {
            languageIds.add(client.getLanguageId());
            languageIds.addAll(getReferencedLanguages(client.getLanguageId())
                    .stream().map(Language::getId).toList());
        }

        for (Integer languageId : languageIds)
            for (Alias alias : aliases)
                if (Util.compareIntegers(alias.getLanguageId(), languageId))
                    return alias;

        return null;
    }

    public List<CompSeasonPhase> getCompSeasonPhaseSiblings(CompSeasonPhaseKey cspk) throws SQLException {
        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);
        CompSeasonPhase csp = cspm.getCompSeasonPhase(cspk);

        CompSeasonPhaseKey parentKey = new CompSeasonPhaseKey(cspk.getSuperKey(), csp.getParentPhaseId());

        return cspm.getPhaseListFromParent(parentKey);
    }

    public void addCompDivisionsToTeams(List<Team> teamList, CompSeasonKey csk) throws SQLException {
        List<CompSeasonTeam> compSeasonTeams = new CompSeasonTeamManager(stat).getTeamsInCompSeason(csk);
        List<CompDivision> compDivisions = new CompDivisionManager(stat).getCompDivisions(csk.getCompetitionId());

        for (Team team : teamList)
            for (CompSeasonTeam compSeasonTeam : compSeasonTeams)
                if (compSeasonTeam.getTeamId() == team.getId()) {
                    Integer compDivisionId = compSeasonTeam.getCompDivisionId();

                    if (compDivisionId != null)
                        for (CompDivision compDivision : compDivisions)
                            if (compDivision.getCompDivisionId() == compDivisionId) {
                                team.setCompDivision(compDivision);
                                break;
                            }

                    break;
                }
    }

    private void copyCompSeasonPhases(CompSeasonKey csk, int seasonId) throws SQLException {
        CompSeasonKey newCsk = new CompSeasonKey(csk.getCompetitionId(), seasonId);

        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);
        List<CompSeasonPhase> compSeasonPhases = cspm.getCompSeasonPhases(csk);

        for (CompSeasonPhase compSeasonPhase : compSeasonPhases) {
            CompSeasonPhaseKey newCspk = new CompSeasonPhaseKey(newCsk,
                    compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId());

            compSeasonPhase.setCompSeasonPhaseKey(newCspk);

            compSeasonPhase.setStartDate(null);
            compSeasonPhase.setEndDate(null);
            compSeasonPhase.setFinished(false);
        }

        insertCompSeasonPhasesFromParents(cspm, compSeasonPhases, new ArrayList<>());
    }

    private void copySportsEvents(CompSeasonKey csk, int seasonId) throws SQLException {
        CompSeasonKey newCsk = new CompSeasonKey(csk.getCompetitionId(), seasonId);

        CompSeasonEventManager csem = new CompSeasonEventManager(stat);
        CompSeasonEventPartManager csepm = new CompSeasonEventPartManager(stat);
        EventDisciplinePartManager edpm = new EventDisciplinePartManager(stat);

        Map<CompSeasonEventKey, CompSeasonEvent> compSeasonEventMap = new HashMap<>() {{
            csem.getCompSeasonEvents(csk).forEach(x -> {
                CompSeasonEventKey cseKey = new CompSeasonEventKey(csk, x.getCompSeasonEventId());
                put(cseKey, x);
            });
        }};
        List<CompSeasonEventKey> compSeasonEventKeys = compSeasonEventMap.keySet().stream().toList();

        List<CompSeasonEventPart> compSeasonEventParts = csepm.getCompSeasonEventPartsFromEvents(compSeasonEventKeys);
        Map<EventDisciplinePartKey, EventDisciplinePart> eventDisciplinePartMap = edpm.getEventDisciplineMapFromEvents(compSeasonEventKeys);

        Map<EventDisciplinePartKey, EventDisciplinePart> newDisciplinesParts = new HashMap<>() {{
            eventDisciplinePartMap.forEach((k, v) -> {
                int compSeasonEventId = k.getSuperKey().getSuperKey().getCompSeasonEventId();
                int eventPartId = k.getSuperKey().getCompSeasonEventPartId();

                CompSeasonEventKey newEventKey = new CompSeasonEventKey(newCsk, compSeasonEventId);
                CompSeasonEventPartKey newEventPartKey = new CompSeasonEventPartKey(newEventKey, eventPartId);
                EventDisciplinePartKey newKey = new EventDisciplinePartKey(newEventPartKey, k.getEventDisciplinePartId());

                put(newKey, v);
            });
        }};

        Map<CompSeasonEventPartKey, CompSeasonEventPart> newEventParts = new HashMap<>() {{
            compSeasonEventParts.forEach(compSeasonEventPart -> {
                int compSeasonEventId = compSeasonEventPart.getCompSeasonEventPartKey().getSuperKey().getCompSeasonEventId();
                CompSeasonEventKey newEventKey = new CompSeasonEventKey(newCsk, compSeasonEventId);
                CompSeasonEventPartKey newEventPartKey = new CompSeasonEventPartKey(newEventKey, compSeasonEventPart.getCompSeasonEventPartId());

                put(newEventPartKey, compSeasonEventPart);
            });
        }};

        Map<CompSeasonEventKey, CompSeasonEvent> newEventMap = new HashMap<>() {{
            compSeasonEventMap.forEach((k, v) -> {
                CompSeasonEventKey newKey = new CompSeasonEventKey(newCsk, k.getCompSeasonEventId());
                put(newKey, new CompSeasonEvent());
            });
        }};

        csem.insertCompSeasonEventMap(newEventMap);
        csepm.insertCompSeasonEventParts(newEventParts);
        edpm.insertEventDisciplineParts(newDisciplinesParts);
    }

    /**
     * Iterative method to insert phases with given parent phases. The inserted phases are the parent phases
     * in the next iteration.
     */
    private void insertCompSeasonPhasesFromParents(CompSeasonPhaseManager cspm,
                                                   List<CompSeasonPhase> compSeasonPhases,
                                                   List<Integer> parentPhases) throws SQLException {
        if (!compSeasonPhases.isEmpty()) {
            List<CompSeasonPhase> phasesToInsert = new ArrayList<>();
            List<Integer> phaseIdsToInsert = new ArrayList<>();

            List<CompSeasonPhase> phasesToDispatch = new ArrayList<>();

            for (CompSeasonPhase compSeasonPhase : compSeasonPhases) {
                int compSeasonPhaseId = compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId();
                Integer parentPhaseId = compSeasonPhase.getParentPhaseId();

                if ((parentPhases.isEmpty() && parentPhaseId == null) || parentPhases.contains(parentPhaseId)) {
                    phasesToInsert.add(compSeasonPhase);
                    phaseIdsToInsert.add(compSeasonPhaseId);
                } else
                    phasesToDispatch.add(compSeasonPhase);
            }

            cspm.insertCompSeasonPhases(phasesToInsert);
            insertCompSeasonPhasesFromParents(cspm, phasesToDispatch, phaseIdsToInsert);
        }
    }

    private Map<String, Person> getPersonNameMapWithNewPersonsForGender(List<String> names, int genderId)
            throws SQLException {
        PersonManager pm = new PersonManager(stat);
        Map<String, Person> personNameMap = pm.getNamePersonMap(names);

        // Always check names for existence in personNameMap, don't try checks on sizes etc. for necessity.
        // String matching in SQL seems to be less accurate than in Java, so the personNameMap
        // may contain extra persons that are not in the names array.
        List<Person> personList = new ArrayList<>();
        int personIdStart = pm.getNewPersonId(), personId = personIdStart;

        for (String name : names)
            if (!personNameMap.containsKey(name)) {
                Person person = new Person();
                person.setId(personId++);
                person.setName(name);
                person.setGenderId(genderId);
                person.setNewlyCreated(true);
                personList.add(person);

                personNameMap.put(name, person);
            }

        pm.insertPersons(personList, personIdStart);

        return personNameMap;
    }

    private Map<String, Person> getPersonNameMapWithNewPersons(List<String> names, Competition comp)
            throws SQLException {
        return getPersonNameMapWithNewPersonsForGender(names, comp.getGenderId());
    }

    private void updatePersonFromDoublePartner(PersonManager pm, Person p, Person dblPrt) throws SQLException {
        if (p.getGenderId() == Gender.genderIdMixed && dblPrt.getGenderId() != Gender.genderIdMixed) {
            p.setGenderId(dblPrt.getGenderId() == Gender.genderIdMale ? Gender.genderIdFemale : Gender.genderIdMale);
            pm.update(p.getId(), p);
        }
    }

    private Map<String, Double> getDoubleNameMapFromKeys(List<DoublePersonSport1PersonSport2Key> keys,
                                                         Map<Integer, PersonSport> personSportMap,
                                                         Map<Integer, Person> personMap) throws SQLException {
        DoubleManager dm = new DoubleManager(stat);

        Map<DoublePersonSport1PersonSport2Key, Double> doubleMap = new LinkedHashMap<>() {{
            Map<DoublePersonSport1PersonSport2Key, Double> existingDoubles = dm.getDoubleMapFromPersonSports(keys);

            keys.forEach(x -> put(x, existingDoubles.get(x)));
        }};

        List<DoublePersonSport1PersonSport2Key> nonExistKeys = keys.stream().filter(x -> doubleMap.get(x) == null)
                .toList();

        if (!nonExistKeys.isEmpty()) {
            int idStart = dm.getNewId(), id = idStart;
            List<Double> newDoubles = new ArrayList<>();

            for (DoublePersonSport1PersonSport2Key key : nonExistKeys) {
                PersonSport ps1 = personSportMap.get(key.getPersonSport1Id());
                PersonSport ps2 = personSportMap.get(key.getPersonSport2Id());

                Double dbl = new Double();
                dbl.setId(id++);
                setPersonSportIdsOnDouble(dbl, ps1.getId(), ps2.getId(), personSportMap);

                dbl.setNewlyCreated(true);
                dbl.setPerson1(personMap.get(ps1.getPersonId()));
                dbl.setPerson2(personMap.get(ps2.getPersonId()));

                newDoubles.add(dbl);
                doubleMap.put(key, dbl);
            }

            setDoubleDescriptions(newDoubles, personSportMap);

            dm.insertDoubles(newDoubles, idStart);
        }

        return new LinkedHashMap<>() {{
            doubleMap.forEach((k, v) -> put(v.getDescription(), v));
        }};
    }

    private void dedoublePersonSports(int personFromId, int personToId) throws SQLException {
        PersonSportManager psm = new PersonSportManager(stat);
        List<PersonSport> personSportsFrom = psm.getPersonSportsForPerson(personFromId);
        List<PersonSport> personSportsTo = getPersonSportsTo(personToId, personSportsFrom);

        Comparator<PersonSport> comparator = new PersonSportSportId();

        personSportsFrom.sort(comparator);
        personSportsTo.sort(comparator);

        PersonSportDedoubler dedoubler = new PersonSportDedoubler(stat);

        for (int i = 0; i < personSportsFrom.size(); i++) {
            int pFromId = personSportsFrom.get(i).getId();
            int pToId = personSportsTo.get(i).getId();

            dedoubler.dedouble(pFromId, pToId);
        }

        psm.deletePersonSportsForPerson(personFromId);
    }

    private List<PersonSport> getPersonSportsTo(int personToId, List<PersonSport> personSportsFrom) throws SQLException {
        Person personTo = new PersonManager(stat).getEntityFromId(personToId);

        List<PersonSportIdKey> personSportIdKeysTo = new ArrayList<>();

        for (PersonSport personSport : personSportsFrom)
            personSportIdKeysTo.add(new PersonSportIdKey(personToId, personSport.getSportId()));

        PersonSportManager psm = new PersonSportManager(stat);

        List<PersonSport> personSportsTo = psm.getPersonSportsFromPersonSportIds(personSportIdKeysTo);
        List<PersonSport> personSportsToCreate = new ArrayList<>();

        Comparator<PersonSport> comparator = new PersonSportSportId();

        personSportsFrom.sort(comparator);
        personSportsTo.sort(comparator);

        int indXFrom = 0, indXTo = 0;

        while (indXTo < personSportsTo.size()) {
            PersonSport personSportFrom = personSportsFrom.get(indXFrom);
            PersonSport personSportTo = personSportsTo.get(indXTo);

            if (personSportFrom.getSportId() != personSportTo.getSportId()) {
                personSportsToCreate.add(getNewPersonSport(personTo, personSportFrom));
                indXTo--;
            }

            indXFrom++;
            indXTo++;
        }

        for (int i = indXFrom; i < personSportsFrom.size(); i++) {
            PersonSport personSportFrom = personSportsFrom.get(i);
            personSportsToCreate.add(getNewPersonSport(personTo, personSportFrom));
        }

        if (!personSportsToCreate.isEmpty()) {
            insertNewPersonSports(psm, personSportsToCreate);
            personSportsTo.addAll(personSportsToCreate);
        }

        return personSportsTo;
    }

    private PersonSport getNewPersonSport(Person personTo, PersonSport existingPersonSport) {
        PersonSport newPersonSport = new PersonSport();
        newPersonSport.setPersonId(personTo.getId());
        newPersonSport.setSportId(existingPersonSport.getSportId());
        newPersonSport.setDescription(personTo.getName());
        newPersonSport.setElo(existingPersonSport.getElo());

        return newPersonSport;
    }

    private void insertNewPersonSports(PersonSportManager psm, List<PersonSport> personSports) throws SQLException {
        int idStart = psm.getNewId(), id = idStart;

        for (PersonSport ps : personSports) {
            ps.setId(id++);
            ps.setNewlyCreated(true);
        }

        psm.insertPersonSports(personSports, idStart);
    }

    private String getGeoOutputString(Geo geo, Map<Integer, GeoType> geoTypes, Map<Integer, Geo> parentGeos) {
        String[] stringParts = new String[2 + (geo.getParentGeoId() != null ? 1 : 0)];
        stringParts[0] = geo.getName();
        stringParts[1] = geoTypes.get(geo.getGeoTypeId()).getName();
        if (geo.getParentGeoId() != null)
            stringParts[2] = parentGeos.get(geo.getParentGeoId()).getName();

        return Util.concatStrings(stringParts, " | ");
    }

    private List<Language> getReferencedLanguages(int languageId) throws SQLException {
        List<Language> referencedLanguages = new ArrayList<>();

        LanguageManager lm = new LanguageManager(stat);
        Language curLan = lm.getEntityFromId(languageId);

        while (curLan != null)
            if (curLan.getFallbackLanguageId() != null) {
                curLan = lm.getEntityFromId(curLan.getFallbackLanguageId());
                referencedLanguages.add(curLan);
            } else
                curLan = null;

        return referencedLanguages;
    }

    private Map<String, PersonSport> getDescrPersonSportMap(List<Person> persons, int competitionId)
            throws SQLException {
        List<PersonSport> personSports = getPersonSportsWithNewInstances(getSportId(competitionId), persons);

        Map<String, PersonSport> personSportMap = new HashMap<>();
        for (PersonSport personSport : personSports)
            personSportMap.put(personSport.getDescription(), personSport);

        return personSportMap;
    }

    private Sport getSport(List<Sport> sports, int sportId) {
        for (Sport sport : sports)
            if (sport.getId() == sportId)
                return sport;

        return null;
    }
}
