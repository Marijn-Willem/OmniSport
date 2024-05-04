package com.sportservlet.html;

import com.sports.calc.alcifo.EventPartTeamFactory;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonEventPartKey;
import jakarta.servlet.http.HttpServletRequest;

public abstract class ManageEventPartTeams extends ManageAlcifoPartParticipants<Team, CompSeasonEventPartKey> {
    @Override
    CompSeasonEventPartKey getPartKey(HttpServletRequest req) {
        return getCompSeasonEventPartKey(req);
    }

    @Override
    EventPartTeamFactory getFactory() {
        return new EventPartTeamFactory();
    }

    @Override
    void initSpecificJsProperties() {
        jsList.add("eventpartteam");
    }
}
