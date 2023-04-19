package com.management.servlet.html;

import com.sportservlet.html.AbstractHtmlServlet;
import jakarta.servlet.http.HttpServletRequest;

public abstract class SuperHtmlServlet extends com.sportservlet.html.SuperHtmlServlet implements AbstractHtmlServlet {
    @Override
    protected void initProperties(HttpServletRequest req) {
        cssList.add("styling");
        jsList.add("general");
        jsList.add("entity");
        initSpecificProperties(req);
    }
}
