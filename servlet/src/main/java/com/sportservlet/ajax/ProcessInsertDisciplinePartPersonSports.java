package com.sportservlet.ajax;

import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartPersonSportFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProcessInsertDisciplinePartPersonSports extends ProcessInsertAlcifoPartParticipants<EventDisciplinePartKey> {
    @Override
    EventDisciplinePartKey getPartKey(Statement stat, HttpServletRequest req) throws SQLException {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(stat, req), edpid);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new DisciplinePartPersonSportFactory();
    }

    @Override
    boolean specificCheckBeforeInsert(Statement stat) throws SQLException {
        CompSeasonEventPartKey csepKey = partKey.getSuperKey();
        List<EventPartPersonSport> eventPartPersonSports = new EventPartPersonSportManager(stat)
                .getPartParticipantList(csepKey);

        return eventPartPersonSports.size() > 0;
    }

    @Override
    String getOutputSpecificCheckFail() {
        return "No person sports in event part";
    }
}
