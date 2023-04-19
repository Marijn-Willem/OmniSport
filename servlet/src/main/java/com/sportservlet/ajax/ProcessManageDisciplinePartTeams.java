package com.sportservlet.ajax;

import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartTeamFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageDisciplinePartTeams extends ProcessManageAlcifoPartParticipants {
    @Override
    EventDisciplinePartKey getPartKey(Statement stat, HttpServletRequest req) throws SQLException {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(stat, req), edpid);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new DisciplinePartTeamFactory();
    }
}
