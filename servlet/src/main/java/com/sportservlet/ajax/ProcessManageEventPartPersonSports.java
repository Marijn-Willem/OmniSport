package com.sportservlet.ajax;

import com.sports.calc.alcifo.EventPartPersonSportFactory;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageEventPartPersonSports extends ProcessManageAlcifoPartParticipants<CompSeasonEventPartKey, EventPartPersonSport> {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    EventPartPersonSportFactory getFactory() {
        return new EventPartPersonSportFactory();
    }
}
