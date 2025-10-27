package com.darts.servlet.ajax;

public class MatchesParentMatch extends com.sportservlet.ajax.MatchesParentMatch {
    @Override
    protected String getMatchInfoLink() { return "PrepareMatchStats"; }

    @Override
    protected String getMatchLiveLink() { return "PrepareLiveMatch"; }

    @Override
    protected String getManageMatchLink() { return "ManageDartsMatch"; }
}
