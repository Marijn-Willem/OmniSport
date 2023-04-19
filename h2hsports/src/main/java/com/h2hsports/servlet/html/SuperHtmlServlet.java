package com.h2hsports.servlet.html;

import com.sportservlet.html.AbstractHtmlServlet;
import jakarta.servlet.http.HttpServletRequest;

abstract class SuperHtmlServlet extends com.sportservlet.html.SuperHtmlServlet implements AbstractHtmlServlet {
    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        cssList.add("styling");

        initSpecificProperties(req);
    }
}
