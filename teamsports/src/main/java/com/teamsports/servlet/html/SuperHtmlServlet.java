package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public abstract class SuperHtmlServlet extends com.sportservlet.html.SuperHtmlServlet {
    abstract void initSpecificProperties(HttpServletRequest req);

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        cssList.add("styling");

        initSpecificProperties(req);
    }
}
