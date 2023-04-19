package com.darts.servlet.ajax;

import com.sports.entity.H2HMatch;

public class MatchesCompSeasonPhase extends com.sportservlet.ajax.MatchesCompSeasonPhase {
    protected String getMatchInfoLink() {
        return "PrepareMatchStats";
    }

    protected String getMatchLiveLink() {
        return "PrepareLiveMatch";
    }

    protected String getManageMatchLink() {
        return "ManageDartsMatch";
    }

    @Override
    protected boolean showMatchLiveLink(H2HMatch match) {
        return super.showMatchLiveLink(match) &&
                match.getParticipant1Id() != null &&
                match.getParticipant2Id() != null;
    }
}
