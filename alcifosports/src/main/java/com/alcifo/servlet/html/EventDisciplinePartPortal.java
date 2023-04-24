package com.alcifo.servlet.html;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class EventDisciplinePartPortal extends com.sportservlet.html.EventDisciplinePartPortal {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPartPortal?" + compSeasonUrlParameters + "&eid=" +
                getIntValuedParameterValue(req, "eid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected void writeSpecificPart(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        int eid = getIntValuedParameterValue(req, "eid");
        int csepid = getIntValuedParameterValue(req, "csepid");

        CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(new CompSeasonEventKey(compSeasonKey, eid), csepid);
        boolean hasFixedParts = new DbCalculation(stat).hasDisciplineParts(csepKey);

        if (!hasFixedParts) {
            ServletUtil.writeGenericGoToButton("ManageEventDisciplinePart", compSeasonUrlParameters +
                    "&eid=" + eid + "&csepid=" + csepid + "&md=i", "Add Event Discipline Part", w);
        }
    }
}
