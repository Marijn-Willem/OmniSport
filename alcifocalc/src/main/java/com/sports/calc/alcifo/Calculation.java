package com.sports.calc.alcifo;

import com.sports.entity.*;
import com.sports.entity.key.SportEventKey;
import com.sports.logic.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Calculation {
    public static AlcifoParticipantFactory getAlcifoParticipantFactory(SportEvent sportEvent) {
        return sportEvent.isTeam() ? new EventTeamFactory() : new EventPersonSportFactory();
    }

    public static List<DisciplinePartPersonSport> getDisciplinePartPersonsSorted(List<DisciplinePartPersonSport> disciplinePartPeople,
                                                                                 List<EventDisciplinePart> eventDisciplinePartsSorted) {
        List<DisciplinePartPersonSport> disciplinePartPeopleSorted = new ArrayList<>();

        for (EventDisciplinePart eventDisciplinePart : eventDisciplinePartsSorted)
            for (DisciplinePartPersonSport disciplinePartPersonSport : disciplinePartPeople)
                if (disciplinePartPersonSport.getEventDisciplinePartId() == eventDisciplinePart.getEventDisciplinePartId()) {
                    disciplinePartPeopleSorted.add(disciplinePartPersonSport);
                    break;
                }

        return disciplinePartPeopleSorted;
    }

    public static String getPointsAsString(int resultTypeId, Integer resultTypePrecisionId, int points) {
        if (resultTypeId == ResultType.resultTypeIdTime)
            return resultTypePrecisionId != null ? Util.getTimeStringFromMillis(points, resultTypePrecisionId) :
                    Util.getHMSStringFromMillis(points);

        return Integer.toString(points);
    }

    public static boolean isCyclingRoadSingleRace(SportEventKey sportEventKey) {
        return sportEventKey.getSportId() == Sport.sportIdCyclingRoad && (
                sportEventKey.getSportEventId() == SportEvent.sportEventIdCyclingRoadSingleMale ||
                        sportEventKey.getSportEventId() == SportEvent.sportEventIdCyclingRoadSingleFemale
        );
    }
}
