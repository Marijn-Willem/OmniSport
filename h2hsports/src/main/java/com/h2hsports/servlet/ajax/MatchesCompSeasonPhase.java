package com.h2hsports.servlet.ajax;

public class MatchesCompSeasonPhase extends com.sportservlet.ajax.MatchesCompSeasonPhase {
    protected String getMatchInfoLink() {
        return "MatchInfo";
    }

    protected String getMatchLiveLink() {
        return "AddMatchScore";
    }

    protected String getManageMatchLink() {
        return "ManageH2HMatch";
    }
}
