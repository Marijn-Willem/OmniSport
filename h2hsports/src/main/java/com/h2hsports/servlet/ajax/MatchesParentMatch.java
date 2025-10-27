package com.h2hsports.servlet.ajax;

public class MatchesParentMatch extends com.sportservlet.ajax.MatchesParentMatch {
    @Override
    protected String getMatchInfoLink() { return "MatchInfo"; }

    @Override
    protected String getMatchLiveLink() { return "AddMatchScore"; }

    @Override
    protected String getManageMatchLink() { return "ManageH2HMatch"; }
}
