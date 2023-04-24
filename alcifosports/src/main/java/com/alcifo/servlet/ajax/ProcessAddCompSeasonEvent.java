package com.alcifo.servlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessAddCompSeasonEvent extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int eid = getIntValuedParameterValue(req, "eid");

        CompSeasonEventKey csek = new CompSeasonEventKey(compSeasonKey, eid);
        new DbCalculation(stat).insertCompSeasonEvent(csek, new CompSeasonEvent());

        resp.getWriter().append("Event successfully added");
    }
}
