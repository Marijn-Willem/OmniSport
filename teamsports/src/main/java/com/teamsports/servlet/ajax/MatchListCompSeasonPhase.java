package com.teamsports.servlet.ajax;

import com.sports.entity.CompSeasonPhase;
import com.sports.entity.TeamMatch;
import com.sports.entity.manager.TeamMatchManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchListCompSeasonPhase extends MatchList {
    @Override
    protected List<TeamMatch> getMatchList(TeamMatchManager mm, CompSeasonPhase csp) throws SQLException {
        // Construct ArrayList, because it gets sorted in the super class.
        return new ArrayList<>() {{
            mm.getPlayedMatchesInCompSeasonPhase(csp.getCompSeasonPhaseKey())
                    .forEach(x -> {
                        if (!csp.isHasParentMatches() || x.getParentMatchId() == null)
                            add(x);
                    });
        }};
    }
}
