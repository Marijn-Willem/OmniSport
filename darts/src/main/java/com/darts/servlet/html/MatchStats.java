package com.darts.servlet.html;

import com.sports.calc.darts.DbCalculation;
import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.PersonMatch;
import com.sports.entity.PersonMatchPart;
import com.sports.entity.comparator.PersonMatchPartId;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.manager.PersonMatchPartManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MatchStats extends SuperHtmlServlet {
    protected void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("matchstats");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"fillSetStats();\">\n");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        PersonMatchKey pmk = (PersonMatchKey)req.getSession().getAttribute("pmk");
        PersonMatch pm = (PersonMatch)req.getSession().getAttribute("pm");

        return "MatchOverview?cid=" + pmk.getCompetitionId() + "&sid=" + pmk.getSeasonId() +
                "&pid=" + pm.getCompSeasonPhaseId();
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        PersonMatchKey pmk = (PersonMatchKey)req.getSession().getAttribute("pmk");
        PersonMatch pm = (PersonMatch)req.getSession().getAttribute("pm");

        StatObject statObject = new DbCalculation(stat).getMatchWithStats(pmk, pm.getPersonSport1Id());

        Writer w = res.getWriter();
        w.append("<table border=\"1\">\n");

        String rule = "<tr><th>" + pm.getPerson1Name() + "</th><th>Stat</th><th>" +
                pm.getPerson2Name() + "</th></tr>\n";

        w.append(rule);

        w.append(statObject.getStatsTableRows());
        w.append("</table>\n");

        w.append("<div>\n<select id=\"mpid\" onchange=\"fillSetStats();\">\n");

        List<PersonMatchPart> setList = new PersonMatchPartManager(stat).getPersonMatchPartsWithoutParent(pmk);
        setList.sort(new PersonMatchPartId());

        for (PersonMatchPart set : setList) {
            rule = "<option value=\"" + set.getPersonMatchPartId() + "\">" + set.getName() + "</option>\n";
            w.append(rule);
        }

        w.append("</select>\n</div>\n<table id=\"tbl_set_stats\" border=\"1\">\n</table>\n");
    }
}
