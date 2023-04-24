package com.sportservlet.ajax;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.EventPartTeamFactory;
import com.sports.entity.key.CompSeasonEventPartKey;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageEventPartTeams extends ProcessManageAlcifoPartParticipants {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new EventPartTeamFactory();
    }
}
