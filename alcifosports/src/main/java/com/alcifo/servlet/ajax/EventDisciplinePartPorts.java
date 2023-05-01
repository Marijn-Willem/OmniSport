package com.alcifo.servlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class EventDisciplinePartPorts extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int cseid = getIntValuedParameterValue(req, "cseid");
        int csepid = getIntValuedParameterValue(req, "csepid");
        int edpid = getIntValuedParameterValue(req, "edpid");
        CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(new CompSeasonEventKey(compSeasonKey, cseid), csepid);

        boolean hasFixedParts = new DbCalculation(stat).hasDisciplineParts(csepKey);

        String md = hasFixedParts ? "r" : "u";

        Writer w = resp.getWriter();

        ServletUtil.writeGenericGoToButton("ManageEventDisciplinePart", compSeasonUrlParameters +
                "&cseid=" + cseid + "&csepid=" + csepid + "&edpid=" + edpid + "&md=" + md, "Manage Event Discipline Part", w);
    }
}
