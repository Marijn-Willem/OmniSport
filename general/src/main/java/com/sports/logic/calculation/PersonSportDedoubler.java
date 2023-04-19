package com.sports.logic.calculation;

import com.sports.entity.Double;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.factory.CompSeasonPersonSportFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sports.logic.calculation.Calculation.*;

class PersonSportDedoubler extends CompSeasonParticipantDedoubler<CompSeasonPersonSportKey, SuperKeyEntity> {
    CompSeasonPersonSportFactory getFactory() {
        return new CompSeasonPersonSportFactory();
    }

    PersonSportDedoubler(Statement stat) {
        super(stat);
    }

    @Override
    void dedoubleSpecific(int pFromId, int pToId) throws SQLException {
        CompSeasonPersonSportManager cspsm = new CompSeasonPersonSportManager(stat);
        CompSeasonTeamPersonSportManager cstpsm = new CompSeasonTeamPersonSportManager(stat);

        List<CompSeasonTeamPersonSportKey> cstpsKeysFrom = cstpsm.getTeamPersonSports(cspKeysFrom);
        List<CompSeasonTeamPersonSportKey> csptsKeysTo = new ArrayList<>();

        List<CompSeasonPersonSportKey> cspsKeysTo = cspsm.getCompSeasonsFromParticipant(pToId);
        List<CompSeasonTeamPersonSportKey> csptsKeysToExisting = cstpsm.getTeamPersonSports(cspsKeysTo);

        for (CompSeasonTeamPersonSportKey keyFrom : cstpsKeysFrom) {
            CompSeasonTeamPersonSportKey keyTo = new CompSeasonTeamPersonSportKey(keyFrom.getSuperKey(), pToId);

            if (!csptsKeysToExisting.contains(keyTo))
                csptsKeysTo.add(keyTo);
        }

        cstpsm.insertTeamPersonSports(csptsKeysTo);
        cstpsm.deleteTeamPersonSports(cspKeysFrom);

        dedoubleDouble(pFromId, pToId);
        dedoubleEventPersonSports(pFromId, pToId);
    }

    private void dedoubleDouble(int personSportFromId, int personSportToId) throws SQLException {
        DoubleManager dm = new DoubleManager(stat);

        Map<DoublePersonSport1PersonSport2Key, Double> doubleMapFrom = dm.getDoublesWithPersonSport(personSportFromId);
        Map<DoublePersonSport1PersonSport2Key, Double> doubleMapTo = dm.getDoublesWithPersonSport(personSportToId);

        Map<DoublePersonSport1PersonSport2Key, Double> doublesToDedouble = new HashMap<>();
        Map<DoublePersonSport1PersonSport2Key, Double> doublesToReplace = new HashMap<>();

        List<Integer> personSportIdsDoubles = new ArrayList<>();
        personSportIdsDoubles.add(personSportFromId);
        personSportIdsDoubles.add(personSportToId);

        addPersonSportIdsToList(doubleMapFrom, personSportIdsDoubles);
        addPersonSportIdsToList(doubleMapTo, personSportIdsDoubles);

        Map<Integer, PersonSport> psMap = new PersonSportManager(stat).getPersonSportMap(personSportIdsDoubles);

        for (Map.Entry<DoublePersonSport1PersonSport2Key, Double> me : doubleMapFrom.entrySet())
            if (doubleMapTo.containsKey(getDoubleKeyWithPersonTo(me.getKey(), personSportFromId, personSportToId, psMap)))
                doublesToDedouble.put(me.getKey(), me.getValue());
            else
                doublesToReplace.put(me.getKey(), me.getValue());

        replacePersonIdOnDoubles(doublesToReplace, personSportFromId, personSportToId);

        DoubleDedoubler dedoubler = new DoubleDedoubler(stat);

        for (Map.Entry<DoublePersonSport1PersonSport2Key, Double> me : doublesToDedouble.entrySet()) {
            dedoubler.dedouble(me.getValue().getId(),
                    doubleMapTo.get(getDoubleKeyWithPersonTo(me.getKey(), personSportFromId, personSportToId, psMap)).getId());

            dm.delete(me.getValue().getId());
        }
    }

    private void dedoubleEventPersonSports(int psFromId, int psToId) throws SQLException {
        EventPersonSportManager epm = new EventPersonSportManager(stat);
        EventPartPersonSportManager eppm = new EventPartPersonSportManager(stat);
        DisciplinePartPersonSportManager dppm = new DisciplinePartPersonSportManager(stat);

        Map<EventPersonSportKey, EventPersonSport> epsFrom = epm.getEventPersonSportMap(psFromId);
        Map<EventPersonSportKey, EventPersonSport> epsTo = new HashMap<>();

        epsFrom.forEach((k, v) -> epsTo.put(new EventPersonSportKey(k.getSuperKey(), psToId), v));

        List<EventPersonSportKey> epKeysFrom = new ArrayList<>(epsFrom.keySet());
        List<EventPartPersonSportKey> eppKeysFrom = new ArrayList<>();

        Map<EventPartPersonSportKey, EventPartPersonSport> eppMapFrom = eppm.getEventPartPersonSportMap(epKeysFrom);
        Map<EventPartPersonSportKey, EventPartPersonSport> eppMapTo = new HashMap<>();

        eppMapFrom.forEach((k, v) -> {
            eppKeysFrom.add(k);

            EventPartPersonSportKey keyTo = new EventPartPersonSportKey(k.getSuperKey(), psToId);
            eppMapTo.put(keyTo,v);
        });

        Map<DisciplinePartPersonSportKey, DisciplinePartPersonSport> dppMapFrom = dppm.getDisciplinePartPersonSportMap(eppKeysFrom);

        List<DisciplinePartPersonSportKey> dppKeysFrom = new ArrayList<>();
        Map<DisciplinePartPersonSportKey, DisciplinePartPersonSport> dppMapTo = new HashMap<>();

        for (Map.Entry<DisciplinePartPersonSportKey, DisciplinePartPersonSport> me : dppMapFrom.entrySet()) {
            dppKeysFrom.add(me.getKey());

            DisciplinePartPersonSportKey keyTo = new DisciplinePartPersonSportKey(
                    new EventPartPersonSportKey(me.getKey().getSuperKey().getSuperKey(), psToId),
                    me.getKey().getEventDisciplinePartId());

            dppMapTo.put(keyTo, me.getValue());
        }

        epm.insertNonExistingEventPersonSports(epsTo);
        eppm.insertEventPartPersonSportMap(eppMapTo);
        dppm.insertDisciplinePartPersonSports(dppMapTo);

        dppm.deleteDisciplinePartPersonSports(dppKeysFrom);
        eppm.deleteEventPartPersonSports(eppKeysFrom);
        epm.deleteEventPersonSports(epKeysFrom);
    }

    private void addPersonSportIdsToList(Map<DoublePersonSport1PersonSport2Key, Double> doubleMap, List<Integer> pIdList) {
        for (Map.Entry<DoublePersonSport1PersonSport2Key, Double> me : doubleMap.entrySet()) {
            pIdList.add(me.getValue().getPersonSport1Id());
            pIdList.add(me.getValue().getPersonSport2Id());
        }
    }

    private DoublePersonSport1PersonSport2Key getDoubleKeyWithPersonTo(DoublePersonSport1PersonSport2Key dblKey,
                                                                       int personSportFromId, int personSportToId,
                                                                       Map<Integer, PersonSport> personSportMap) {
        int personIdToKeep = dblKey.getPersonSport1Id() == personSportFromId ? dblKey.getPersonSport2Id() : dblKey.getPersonSport1Id();

        return getDoubleKey(personSportToId, personIdToKeep, personSportMap);
    }

    private void replacePersonIdOnDoubles(Map<DoublePersonSport1PersonSport2Key, Double> doubleMap,
                                          int personSportFromId, int personSportToId) throws SQLException {
        DoubleManager dm = new DoubleManager(stat);

        List<Integer> personSportIds = new ArrayList<>();
        personSportIds.add(personSportToId);

        for (Map.Entry<DoublePersonSport1PersonSport2Key, Double> me : doubleMap.entrySet()) {
            Double dbl = me.getValue();
            personSportIds.add(dbl.getPersonSport1Id() == personSportFromId ? dbl.getPersonSport2Id() : dbl.getPersonSport1Id());
        }

        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(personSportIds);

        List<Double> doubleList = new ArrayList<>();

        for (Map.Entry<DoublePersonSport1PersonSport2Key, Double> me : doubleMap.entrySet()) {
            Double dbl = me.getValue();

            int idToKeep = dbl.getPersonSport1Id() == personSportFromId ? dbl.getPersonSport2Id() : dbl.getPersonSport1Id();
            setPersonSportIdsOnDouble(dbl, personSportToId, idToKeep, personSportMap);

            doubleList.add(dbl);
        }

        setDoubleDescriptions(doubleList, personSportMap);
        dm.updateDoubleMap(doubleMap);
    }
}
