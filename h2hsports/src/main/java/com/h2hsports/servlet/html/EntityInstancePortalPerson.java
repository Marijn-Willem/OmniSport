package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class EntityInstancePortalPerson extends com.sportservlet.html.EntityInstancePortalPerson {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
