package com.teamsports.servlet.ajax;

import com.sports.entity.key.CompSeasonDivisionKey;
import com.sports.entity.manager.CompSeasonDivisionManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProcessManageCompSeasonDivisions extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String[] divisionIds = req.getParameterValues("did");

        if (divisionIds != null) {
            List<CompSeasonDivisionKey> keys = new ArrayList<>();

            for (String divisionId : divisionIds)
                keys.add(new CompSeasonDivisionKey(compSeasonKey, Integer.parseInt(divisionId)));

            new CompSeasonDivisionManager(stat).insertCompSeasonDivisions(keys);

            resp.getWriter().append("Divisions inserted succesfully");
        }
        else
            resp.getWriter().append("No divisions selected");
    }
}
