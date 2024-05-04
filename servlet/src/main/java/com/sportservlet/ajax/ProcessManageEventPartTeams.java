package com.sportservlet.ajax;

import com.sports.calc.alcifo.EventPartTeamFactory;
import com.sports.entity.EventPartTeam;
import com.sports.entity.key.CompSeasonEventPartKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageEventPartTeams extends ProcessManageAlcifoPartParticipants<CompSeasonEventPartKey, EventPartTeam> {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    EventPartTeamFactory getFactory() {
        return new EventPartTeamFactory();
    }
}
