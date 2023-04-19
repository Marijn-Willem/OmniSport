package com.teamsports.servlet.ajax;

public class MatchesCompSeasonPhase extends com.sportservlet.ajax.MatchesCompSeasonPhase {
    @Override
    protected String getMatchInfoLink() {
        return "MatchTimeLine";
    }

    @Override
    protected String getMatchLiveLink() {
        return "ManageH2HMatch";
    }

    @Override
    protected String getManageMatchLink() {
        return "ManageH2HMatch";
    }
}
