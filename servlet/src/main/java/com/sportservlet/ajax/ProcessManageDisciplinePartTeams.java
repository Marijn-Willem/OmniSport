package com.sportservlet.ajax;

import com.sports.calc.alcifo.DisciplinePartTeamFactory;
import com.sports.entity.DisciplinePartTeam;
import com.sports.entity.key.EventDisciplinePartKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageDisciplinePartTeams extends ProcessManageAlcifoPartParticipants<EventDisciplinePartKey, DisciplinePartTeam> {
    @Override
    EventDisciplinePartKey getPartKey(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(req), edpid);
    }

    @Override
    DisciplinePartTeamFactory getFactory() {
        return new DisciplinePartTeamFactory();
    }
}
