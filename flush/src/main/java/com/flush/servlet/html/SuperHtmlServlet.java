package com.flush.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

abstract class SuperHtmlServlet extends com.sportservlet.html.SuperHtmlServlet {
    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        cssList.add("styling");
    }
}
