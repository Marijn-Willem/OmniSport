package com.sportservlet.html;

import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.EventPartPersonSportFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageEventPartPersonSports extends ManageAlcifoPartParticipants {
    @Override
    CompSeasonEventPartKey getPartKey(Statement stat, HttpServletRequest req) throws SQLException {
        return getCompSeasonEventPartKey(stat, req);
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
