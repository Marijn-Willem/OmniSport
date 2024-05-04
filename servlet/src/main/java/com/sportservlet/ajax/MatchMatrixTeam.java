package com.sportservlet.ajax;

import com.sports.entity.Team;
import com.sports.logic.factory.CompSeasonTeamFactory;

public class MatchMatrixTeam extends MatchMatrix<Team> {
    @Override
    protected CompSeasonTeamFactory getFactory() {
        return new CompSeasonTeamFactory();
    }
}
