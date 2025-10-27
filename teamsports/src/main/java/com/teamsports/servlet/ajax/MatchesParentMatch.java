package com.teamsports.servlet.ajax;

public class MatchesParentMatch extends com.sportservlet.ajax.MatchesParentMatch {
    @Override
    protected String getMatchInfoLink() { return "MatchTimeLine"; }

    @Override
    protected String getMatchLiveLink() { return "ManageH2HMatch"; }

    @Override
    protected String getManageMatchLink() { return "ManageH2HMatch"; }
}
