package com.sportservlet.ajax;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.EventPartPersonSportFactory;
import com.sports.entity.key.CompSeasonEventPartKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessInsertEventPartPersonSports extends ProcessInsertAlcifoPartParticipants<CompSeasonEventPartKey> {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new EventPartPersonSportFactory();
    }
}
