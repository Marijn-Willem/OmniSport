package com.sportservlet.ajax;

import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.key.CompSeasonTeamPersonSportKey;
import com.sports.entity.manager.CompSeasonTeamPersonSportManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessManageCompSeasonTeamPersonSports extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int tid = getIntValuedParameterValue(req, "tid");
        String[] psIds = req.getParameterValues("psid");

        List<CompSeasonTeamPersonSportKey> keys = Arrays.stream(psIds)
                .map(psid -> new CompSeasonTeamPersonSportKey(new CompSeasonTeamKey(compSeasonKey, tid), Integer.parseInt(psid)))
                .collect(Collectors.toList());

        new CompSeasonTeamPersonSportManager(stat).insertTeamPersonSports(keys);

        resp.getWriter().append("Persons successfully added to team");
    }
}
