package com.darts.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public abstract class SuperHtmlServlet extends com.sportservlet.html.SuperHtmlServlet {
    protected abstract void initSpecificProperties(HttpServletRequest req);

    protected void initProperties(HttpServletRequest req) {
        cssList.add("styling");
        jsList.add("general");

        initSpecificProperties(req);
    }
}
