package com.sports.calc.speedskating;

import com.sports.entity.*;
import com.sports.entity.comparator.EventPartPersonPersonId;
import com.sports.entity.comparator.EventPartPersonResPoints;
import com.sports.entity.comparator.SportEventPartId;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartPersonSportKey;
import com.sports.entity.key.SportEventPartKey;
import com.sports.entity.manager.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record DbCalculation(Statement stat) {
    public List<EventPartPersonSport> getTotalRanking(CompSeasonEventPartKey csepk) throws SQLException {
        CompSeasonEventKey csek = csepk.getSuperKey();

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepk);
        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(csek);
        SportEventPartKey sepk = new SportEventPartKey(compSeasonEvent.getSportEventKey(),
                compSeasonEventPart.getSportEventPartId());

        SportEventPartManager sepm = new SportEventPartManager(stat);

        SportEventPart sep = sepm.getSportEventPart(sepk);
        List<SportEventPart> sportEventParts =
                sepm.getSportEventParts(sepk.getSuperKey(), "\"order\" <= " + sep.getOrder());

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

        List<EventPartPersonSport> ranking = new ArrayList<>();
        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(personSportIds);

        int indX1 = 0;

        while (indX1 < eventPartPeople.size()) {
            EventPartPersonSport eventPartPersonSport = eventPartPeople.get(indX1);
            int indX2 = indX1 + 1;

            while (indX2 < eventPartPeople.size() &&
                    eventPartPeople.get(indX2).getPersonSportId() == eventPartPersonSport.getPersonSportId())
                indX2++;

            if (indX2 - indX1 == sportEventParts.size()) {
                eventPartPersonSport.setPersonSportDescription(personSportMap.get(eventPartPersonSport.getPersonSportId()).getDescription());

                for (int j = 0; j < sportEventParts.size(); j++) {
                    SportEventPart sportEventPart = sportEventParts.get(j);
                    int weight = sportEventPart.getWeight() != null ? sportEventPart.getWeight() : 1;
                    double resPoints =
                            (double) (eventPartPeople.get(indX1 + j).getPoints()) /
                                    (double) (1000 * weight);

                    eventPartPersonSport.addResultPoints(resPoints);
                }

                ranking.add(eventPartPersonSport);
            }

            indX1 = indX2;
        }

        ranking.sort(new EventPartPersonResPoints());

        return ranking;
    }
}
