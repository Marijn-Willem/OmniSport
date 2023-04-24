package com.sportservlet.ajax;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartPersonSportFactory;
import com.sports.entity.key.EventDisciplinePartKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageDisciplinePartPersonSports extends ProcessManageAlcifoPartParticipants {
    @Override
    EventDisciplinePartKey getPartKey(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(req), edpid);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new DisciplinePartPersonSportFactory();
    }
}
