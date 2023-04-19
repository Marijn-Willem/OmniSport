package com.flush.servlet.html;

import com.sportservlet.html.AbstractHtmlServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageFlush extends SuperHtmlServlet implements AbstractHtmlServlet {
    abstract void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException;

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "FlushKeyPortal";
    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        super.initProperties(req);
        jsSpecificList.add("flush");
        initSpecificProperties(req);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        processSpecific(stat, req, res);

        Writer w = res.getWriter();

        w.append("<input type=\"button\" onclick=\"flush();\" value=\"Flush\" /><br/>\n");
        w.append("<div id=\"div_res\"></div>\n");
    }
}
