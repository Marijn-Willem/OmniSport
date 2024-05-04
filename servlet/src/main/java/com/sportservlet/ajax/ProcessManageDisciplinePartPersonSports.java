package com.sportservlet.ajax;

import com.sports.calc.alcifo.DisciplinePartPersonSportFactory;
import com.sports.entity.DisciplinePartPersonSport;
import com.sports.entity.key.EventDisciplinePartKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageDisciplinePartPersonSports extends ProcessManageAlcifoPartParticipants<EventDisciplinePartKey, DisciplinePartPersonSport> {
    @Override
    EventDisciplinePartKey getPartKey(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(req), edpid);
    }

    @Override
    DisciplinePartPersonSportFactory getFactory() {
        return new DisciplinePartPersonSportFactory();
    }
}
