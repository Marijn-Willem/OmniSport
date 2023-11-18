package com.teamsports.servlet.html;

import com.sports.entity.H2HMatch;
import com.sports.entity.TeamMatch;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class ManageH2HMatch extends com.sportservlet.html.ManageH2HMatch {
    @Override
    protected void writeSpecificFields(H2HMatch h2HMatch, Writer w) throws IOException {
        writeNumericTextField("Shootout Score Team 1", "p1ss",
                h2HMatch != null ? ((TeamMatch)h2HMatch).getScoreShootoutHome() : null, w);
        writeNumericTextField("Shootout Score Team 2", "p2ss",
                h2HMatch != null ? ((TeamMatch)h2HMatch).getScoreShootoutAway() : null, w);
    }

    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        String url = "tl".equals(req.getParameter("from")) ? "MatchTimeLine" : "MatchOverview";

        return url + "?" + compSeasonUrlParameters + "&pid=" + req.getParameter("pid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("teammatch");
        cssList.add("styling");
    }
}
