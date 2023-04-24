package com.sportservlet.html;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.EventPartPersonSportFactory;
import com.sports.entity.key.CompSeasonEventPartKey;
import jakarta.servlet.http.HttpServletRequest;

public abstract class ManageEventPartPersonSports extends ManageAlcifoPartParticipants {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new EventPartPersonSportFactory();
    }

    @Override
    void initSpecificJsProperties() {
        jsList.add("eventpartpersonsport");
    }
}
