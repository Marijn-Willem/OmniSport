package com.sports.entity.comparator;

import com.sports.entity.CompSeasonPhaseTeamCorrection;

public class CompSeasonPhaseTeamCorrectionDate extends SuperComparator<CompSeasonPhaseTeamCorrection> {
    @Override
    public int compare(CompSeasonPhaseTeamCorrection o1, CompSeasonPhaseTeamCorrection o2) {
        return compareLocalDateTimes(o1.getDate(), o2.getDate());
    }
}
