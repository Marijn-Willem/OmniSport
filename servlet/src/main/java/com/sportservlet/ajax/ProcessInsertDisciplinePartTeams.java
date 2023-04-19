package com.sportservlet.ajax;

import com.sports.entity.EventPartTeam;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.manager.EventPartTeamManager;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartTeamFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProcessInsertDisciplinePartTeams extends ProcessInsertAlcifoPartParticipants<EventDisciplinePartKey> {
    @Override
    EventDisciplinePartKey getPartKey(Statement stat, HttpServletRequest req) throws SQLException {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(stat, req), edpid);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new DisciplinePartTeamFactory();
    }

    @Override
    boolean specificCheckBeforeInsert(Statement stat) throws SQLException {
        CompSeasonEventPartKey csepKey = partKey.getSuperKey();
        List<EventPartTeam> eventPartTeamList = new EventPartTeamManager(stat).getPartParticipantList(csepKey);

        return eventPartTeamList.size() > 0;
    }

    @Override
    String getOutputSpecificCheckFail() {
        return "No teams in event part";
    }
}
