package com.sportservlet.ajax;

import com.sports.calc.alcifo.DisciplinePartTeamFactory;
import com.sports.entity.DisciplinePartTeam;
import com.sports.entity.EventPartTeam;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.DisciplinePartTeamKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.manager.EventPartTeamManager;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProcessInsertDisciplinePartTeams extends ProcessInsertAlcifoPartParticipants<DisciplinePartTeamKey, EventDisciplinePartKey, DisciplinePartTeam> {
    @Override
    EventDisciplinePartKey getPartKey(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(req), edpid);
    }

    @Override
    DisciplinePartTeamFactory getFactory() {
        return new DisciplinePartTeamFactory();
    }

    @Override
    boolean specificCheckBeforeInsert(Statement stat) throws SQLException {
        CompSeasonEventPartKey csepKey = partKey.getSuperKey();
        List<EventPartTeam> eventPartTeamList = new EventPartTeamManager(stat).getPartParticipantList(csepKey);

        return !eventPartTeamList.isEmpty();
    }

    @Override
    String getOutputSpecificCheckFail() {
        return "No teams in event part";
    }
}
