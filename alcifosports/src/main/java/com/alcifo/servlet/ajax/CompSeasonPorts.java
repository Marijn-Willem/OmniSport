package com.alcifo.servlet.ajax;

import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class CompSeasonPorts extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer w = resp.getWriter();

        ServletUtil.writeGenericGoToButton("AddCompSeasonEvent", compSeasonUrlParameters, "Add Event in CompSeason", w);
        ServletUtil.writeGenericGoToButton("CompSeasonEventPortal", compSeasonUrlParameters, "Competition Events", w);
        ServletUtil.writeGenericGoToButton("CopyCompSeason", compSeasonUrlParameters, "Copy Competition Season", w);
    }
}
