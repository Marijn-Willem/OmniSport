package com.sportservlet.ajax;

import com.sports.calc.alcifo.DisciplinePartPersonSportFactory;
import com.sports.entity.DisciplinePartPersonSport;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.DisciplinePartPersonSportKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.manager.EventPartPersonSportManager;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProcessInsertDisciplinePartPersonSports extends ProcessInsertAlcifoPartParticipants<DisciplinePartPersonSportKey, EventDisciplinePartKey, DisciplinePartPersonSport> {
    @Override
    EventDisciplinePartKey getPartKey(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(req), edpid);
    }

    @Override
    DisciplinePartPersonSportFactory getFactory() {
        return new DisciplinePartPersonSportFactory();
    }

    @Override
    boolean specificCheckBeforeInsert(Statement stat) throws SQLException {
        CompSeasonEventPartKey csepKey = partKey.getSuperKey();
        List<EventPartPersonSport> eventPartPersonSports = new EventPartPersonSportManager(stat)
                .getPartParticipantList(csepKey);

        return !eventPartPersonSports.isEmpty();
    }

    @Override
    String getOutputSpecificCheckFail() {
        return "No person sports in event part";
    }
}
