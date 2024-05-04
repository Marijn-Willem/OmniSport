package com.sportservlet.html;

import com.sports.calc.alcifo.EventPartPersonSportFactory;
import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import jakarta.servlet.http.HttpServletRequest;

public abstract class ManageEventPartPersonSports extends ManageAlcifoPartParticipants<PersonSport, CompSeasonEventPartKey> {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    EventPartPersonSportFactory getFactory() {
        return new EventPartPersonSportFactory();
    }

    @Override
    void initSpecificJsProperties() {
        jsList.add("eventpartpersonsport");
    }
}
