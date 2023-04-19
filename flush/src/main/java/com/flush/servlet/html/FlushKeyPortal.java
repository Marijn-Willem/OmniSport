package com.flush.servlet.html;

import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class FlushKeyPortal extends SuperHtmlServlet {
    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        ServletUtil.writeLink(path, "ManageFlushClientAlias", "Client Alias", w);
        ServletUtil.writeLink(path, "ManageFlushCompSeason", "Competition Season", w);
        ServletUtil.writeLink(path, "ManageFlushEntityInstanceCompSeason", "Entity instance with CompSeason", w);
        ServletUtil.writeLink(path, "ManageFlushEntityInstanceNonCompSeason", "Entity instance without CompSeason", w);
        ServletUtil.writeLink(path, "ManageFlushPersonSport", "Person sport", w);
        ServletUtil.writeLink(path, "ManageFlushSport", "Sport", w);
    }
}
