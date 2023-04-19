package com.cyclingroad.servlet.ajax;

import com.sports.calc.cyclingroad.DbCalculation;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessSetGeneralClassificationPoints extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEventPartKey csepKey = getCompSeasonEventPartKey(stat, req);
        Integer stage = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepKey).getStage();

        String output;

        if (stage != null && new DbCalculation(stat).setGeneralClassificationPointsForStage(compSeasonKey, stage))
            output = "Times set";
        else
            output = "No times set";

        resp.getWriter().append(output);
    }
}
