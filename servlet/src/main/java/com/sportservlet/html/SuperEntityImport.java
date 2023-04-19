package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SuperEntityImport extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected abstract String getOnClick(HttpServletRequest req);

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        String line = "<input type=\"submit\" onclick=\"" + getOnClick(req) + ";\" value=\"Import\" />\n<br/>\n";
        w.append(line);
        w.append("<div id=\"resp\">\n</div>\n");
    }
}
