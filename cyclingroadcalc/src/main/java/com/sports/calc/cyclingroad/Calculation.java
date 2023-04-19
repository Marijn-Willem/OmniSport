package com.sports.calc.cyclingroad;

import com.sports.entity.SportEvent;

public class Calculation {
    public static boolean isSingleRace(int sportEventId) {
        return sportEventId == SportEvent.sportEventIdCyclingRoadSingleMale ||
                sportEventId == SportEvent.sportEventIdCyclingRoadSingleFemale;
    }

    public static boolean isStageRace(int sportEventId) {
        return sportEventId == SportEvent.sportEventIdCyclingRoadStageMale ||
                sportEventId == SportEvent.sportEventIdCyclingRoadStageFemale;
    }

    public static boolean isGeneralClassification(int sportEventId) {
        return sportEventId == SportEvent.sportEventIdCyclingRoadGeneralMale ||
                sportEventId == SportEvent.sportEventIdCyclingRoadGeneralFemale;
    }
}
