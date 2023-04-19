package com.sports.calc.darts;

import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.PersonMatch;
import com.sports.entity.PersonMatchPart;
import com.sports.entity.PersonMatchPartStat;
import com.sports.entity.StatType;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.manager.PersonMatchManager;
import com.sports.entity.manager.PersonMatchPartManager;
import com.sports.entity.manager.PersonMatchPartStatManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
}
