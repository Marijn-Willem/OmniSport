package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManageEntityInstance extends com.sportservlet.html.ManageEntityInstance {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
