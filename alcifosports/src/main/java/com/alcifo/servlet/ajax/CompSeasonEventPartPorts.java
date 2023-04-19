package com.alcifo.servlet.ajax;

import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class CompSeasonEventPartPorts extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Writer w = resp.getWriter();

        w.append("<input type=\"button\" value=\"Manage CompSeason Event Part\" ");
        w.append("onclick=\"goToUpdateCompSeasonEventPart();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Manage Event Discipline Parts\" ");
        w.append("onclick=\"goToEventDisciplinePartPortal();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Manage Locations in Event Part\" ");
        w.append("onclick=\"goToEventPartLocationPortal();\" /><br/>\n");
    }
}
