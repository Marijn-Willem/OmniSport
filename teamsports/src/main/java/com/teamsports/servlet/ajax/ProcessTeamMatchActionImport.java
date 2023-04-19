package com.teamsports.servlet.ajax;

import com.sports.calc.teamsports.DbCalculation;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessTeamMatchActionImport extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int spid = getIntValuedParameterValue(req, "spid");

        String file = System.getenv("OMNISPORT_FILELOC") + Util.fileSeparator +
                getServletContext().getInitParameter("fileDir") + Util.fileSeparator +
                "TeamMatchAction" + Util.fileSeparator +
                req.getParameter("fl");

        new DbCalculation(stat).importMatchActions(spid, file);

        resp.getWriter().append("Actions successfully imported");
    }
}
