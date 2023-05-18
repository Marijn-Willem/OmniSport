package com.sports.calc.speedskating;

import com.sports.entity.*;
import com.sports.entity.comparator.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record DbCalculation(Statement stat) {
    public List<PersonSport> getTotalRanking(CompSeasonEventPartKey csepk) throws SQLException {
        CompSeasonEventKey csek = csepk.getSuperKey();

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepk);
        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(csek);

        SportEventPartManager sepm = new SportEventPartManager(stat);

        List<SportEventPart> sportEventParts = sepm.getSportEventParts(compSeasonEvent.getSportEventKey(),
                "\"order\" <= " + compSeasonEventPart.getOrder());

        sportEventParts.sort(new SportEventPartId());

        EventPartPersonSportManager eppm = new EventPartPersonSportManager(stat);
        List<Integer> personSportIds = eppm.getPersonSportIdsCompSeasonEventPart(csepk);

        List<EventPartPersonSportKey> eventPartPersonSportKeys = new ArrayList<>();

        for (SportEventPart sportEventPart : sportEventParts)
            for (Integer personId : personSportIds) {
                int sportEventPartId = sportEventPart.getSportEventPartId();
                eventPartPersonSportKeys.add(new EventPartPersonSportKey(new CompSeasonEventPartKey(csek, sportEventPartId),
                        personId));
            }

        List<EventPartPersonSport> eventPartPeople =
                eppm.getEventPartPersonSportList(eventPartPersonSportKeys, "points IS NOT NULL");
        eventPartPeople.sort(new EventPartPersonPersonId());

        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(personSportIds);
        List<PersonSport> personSportList = new ArrayList<>();

        int indX1 = 0;

        while (indX1 < eventPartPeople.size()) {
            EventPartPersonSport eventPartPersonSport = eventPartPeople.get(indX1);
            int indX2 = indX1 + 1;

            while (indX2 < eventPartPeople.size() &&
                    eventPartPeople.get(indX2).getPersonSportId() == eventPartPersonSport.getPersonSportId())
                indX2++;

            if (indX2 - indX1 == sportEventParts.size()) {
                PersonSport personSport = personSportMap.get(eventPartPersonSport.getPersonSportId());

                for (int j = 0; j < sportEventParts.size(); j++) {
                    SportEventPart sportEventPart = sportEventParts.get(j);
                    int weight = sportEventPart.getWeight() != null ? sportEventPart.getWeight() : 1;
                    double resPoints =
                            (double) (eventPartPeople.get(indX1 + j).getPoints()) /
                                    (double) (1000 * weight);

                    personSport.addResultPoints(resPoints);
                }

                personSportList.add(personSport);
            }

            indX1 = indX2;
        }

        personSportList.sort(new ParticipantResPoints());
        setRanks(personSportList);

        return personSportList;
    }

    public List<EventDisciplinePart> getSortedEventDisciplineParts(CompSeasonEventPartKey csepKey) throws SQLException {
        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getEntityFromSuperKey(csepKey);
        SportDisciplineKey sdk = compSeasonEventPart.getSportDisciplineKey();

        com.sports.calc.alcifo.DbCalculation dbCalc = new com.sports.calc.alcifo.DbCalculation(stat);

        List<EventDisciplinePart> eventDisciplineParts = new EventDisciplinePartManager(stat).getEventDisciplineList(csepKey);
        dbCalc.fillDisciplinePartsForEventDisciplineParts(sdk, eventDisciplineParts);
        eventDisciplineParts.sort(new EventDisciplinePartOrder());

        return eventDisciplineParts;
    }

    private void setRanks(List<PersonSport> personSports) {
        int curRank = 0;

        for (int i = 0; i < personSports.size(); i++) {
            PersonSport ps = personSports.get(i);
            PersonSport psPrev = i > 0 ? personSports.get(i - 1) : null;

            if (psPrev == null || ps.getResultPoints() > psPrev.getResultPoints())
                curRank = i + 1;

            ps.setRank(curRank);
        }
    }
}
