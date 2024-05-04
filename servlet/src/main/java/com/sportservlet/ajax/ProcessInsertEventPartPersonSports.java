package com.sportservlet.ajax;

import com.sports.calc.alcifo.EventPartPersonSportFactory;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartPersonSportKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessInsertEventPartPersonSports extends ProcessInsertAlcifoPartParticipants<EventPartPersonSportKey, CompSeasonEventPartKey, EventPartPersonSport> {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    EventPartPersonSportFactory getFactory() {
        return new EventPartPersonSportFactory();
    }
}
