package com.h2hsports.servlet.ajax;

public class MatchesCompSeasonPhase extends com.sportservlet.ajax.MatchesCompSeasonPhase {
    @Override
    protected String getMatchInfoLink() {
        return "MatchInfo";
    }

    @Override
    protected String getMatchLiveLink() {
        return "AddMatchScore";
    }

    @Override
    protected String getManageMatchLink() {
        return "ManageH2HMatch";
    }

    @Override
    protected String getParentPortalLink() { return "ParentMatchPortal"; }
}
