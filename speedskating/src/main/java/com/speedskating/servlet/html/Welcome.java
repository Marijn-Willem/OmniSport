package com.speedskating.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class Welcome extends SuperHtmlServlet {
    void initSpecificProperties(HttpServletRequest req) {

    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<h1>Welcome!</h1>\n");
        w.append("<div>\n");

        String line = "<a href=\"" + path + "/PersonSearch\">Manage persons</a>\n";
        w.append(line);
        line = "<br/><a href=\"" + path + "/CompSeasonPortal\">Competition seasons</a>\n";
        w.append(line);
        w.append("</div>\n");
    }
}
