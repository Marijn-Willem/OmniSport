package com.sportservlet.html;

import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.EventPartTeamFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageEventPartTeams extends ManageAlcifoPartParticipants {
    @Override
    CompSeasonEventPartKey getPartKey(Statement stat, HttpServletRequest req) throws SQLException {
        return getCompSeasonEventPartKey(stat, req);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new EventPartTeamFactory();
    }

    @Override
    void initSpecificJsProperties() {
        jsList.add("eventpartteam");
    }
}
