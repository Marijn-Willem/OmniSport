package com.teamsports.servlet.ajax;

import com.sports.entity.TeamMatch;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.TeamMatchManager;

import java.sql.SQLException;
import java.util.List;

public class MatchListCompSeasonPhase extends MatchList {
    protected List<TeamMatch> getMatchList(TeamMatchManager mm, CompSeasonPhaseKey cspk) throws SQLException {
        return mm.getPlayedMatchesInCompSeasonPhase(cspk);
    }
}
