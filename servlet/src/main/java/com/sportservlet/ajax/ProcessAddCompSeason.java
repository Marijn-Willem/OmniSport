package com.sportservlet.ajax;

import com.sports.entity.CompSeason;
import com.sports.entity.manager.CompSeasonManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessAddCompSeason extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonManager csm = new CompSeasonManager(stat);
        CompSeason cs = csm.getCompSeason(compSeasonKey);

        Writer w = resp.getWriter();

        if (cs == null) {
            csm.insertCompSeason(compSeasonKey, new CompSeason());
            w.append("Competition Season successfully added");
        }
        else
            w.append("Competition Season already exists");
    }
}
