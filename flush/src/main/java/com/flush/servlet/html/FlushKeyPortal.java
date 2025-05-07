package com.flush.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class FlushKeyPortal extends SuperHtmlServlet {
    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        writeLink("ManageFlushClientAlias", "Client Alias", w);
        writeLink("ManageFlushCompSeason", "Competition Season", w);
        writeLink("ManageFlushEntityInstanceCompSeason", "Entity instance with CompSeason", w);
        writeLink("ManageFlushEntityInstanceNonCompSeason", "Entity instance without CompSeason", w);
        writeLink("ManageFlushPersonSport", "Person sport", w);
        writeLink("ManageFlushSport", "Sport", w);
    }
}
