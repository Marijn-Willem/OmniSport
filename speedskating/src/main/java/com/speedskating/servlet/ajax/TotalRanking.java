package com.speedskating.servlet.ajax;

import com.sports.calc.speedskating.DbCalculation;
import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TotalRanking extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEventPartKey csepk = getCompSeasonEventPartKey(req);

        List<PersonSport> ranking = new DbCalculation(stat).getTotalRanking(csepk);

        Writer w = resp.getWriter();

        for (PersonSport personSport : ranking) {
            double points = personSport.getResultPoints();

            String rule = "<tr><td>" + personSport.getRank() + ".</td><td>" +
                    personSport.getDescription() + "</td>" +
                    "<td>" + Util.getDoubleAsStringWith3Digits(points) + "</td></tr>\n";

            w.append(rule);
        }
    }
}
