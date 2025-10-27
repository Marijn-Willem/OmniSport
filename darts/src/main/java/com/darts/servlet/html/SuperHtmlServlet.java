package com.darts.servlet.html;

import com.sports.entity.PersonMatch;
import com.sports.entity.key.PersonMatchKey;
import jakarta.servlet.http.HttpServletRequest;

public abstract class SuperHtmlServlet extends com.sportservlet.html.SuperHtmlServlet {
    protected abstract void initSpecificProperties(HttpServletRequest req);

    protected void initProperties(HttpServletRequest req) {
        cssList.add("styling");
        jsList.add("general");

        initSpecificProperties(req);
    }

    protected String getReturnPathForDispatchedMatchView(HttpServletRequest req) {
        PersonMatchKey pmk = (PersonMatchKey)req.getSession().getAttribute("pmk");
        PersonMatch pm = (PersonMatch)req.getSession().getAttribute("pm");

        String baseParameterString = "cid=" + pmk.getCompetitionId() + "&sid=" + pmk.getSeasonId() +
                "&pid=" + pm.getCompSeasonPhaseId();

        return pm.getParentMatchId() != null ?
                "ParentMatchPortal?" + baseParameterString + "&pmid=" + pm.getParentMatchId() :
                "MatchOverview?" + baseParameterString;
    }
}
