package com.darts.servlet.ajax;

import com.sports.entity.H2HMatch;

public class MatchesCompSeasonPhase extends com.sportservlet.ajax.MatchesCompSeasonPhase {
    @Override
    protected String getMatchInfoLink() {
        return "PrepareMatchStats";
    }

    @Override
    protected String getMatchLiveLink() {
        return "PrepareLiveMatch";
    }

    @Override
    protected String getManageMatchLink() { return "ManageDartsMatch"; }

    @Override
    protected String getParentPortalLink() { return "ParentMatchPortal"; }

    @Override
    protected boolean showMatchLiveLink(H2HMatch match) {
        return super.showMatchLiveLink(match) &&
                match.getParticipant1Id() != null &&
                match.getParticipant2Id() != null;
    }
}
