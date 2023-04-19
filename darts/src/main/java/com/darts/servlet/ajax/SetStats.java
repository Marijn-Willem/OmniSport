package com.darts.servlet.ajax;

import com.sports.calc.darts.DbCalculation;
import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.PersonMatch;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class SetStats extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        PersonMatchKey personMatchKey = (PersonMatchKey)req.getSession().getAttribute("pmk");
        PersonMatch personMatch = (PersonMatch)req.getSession().getAttribute( "pm");

        int personMatchPartId = Integer.parseInt(req.getParameter("mpid"));

        PersonMatchPartKey personMatchPartKey = new PersonMatchPartKey(personMatchKey, personMatchPartId);
        StatObject statObject = new DbCalculation(stat).getSetWithStats(personMatchPartKey, personMatch.getPersonSport1Id());

        resp.getWriter().append(statObject.getStatsTableRows());
    }
}
