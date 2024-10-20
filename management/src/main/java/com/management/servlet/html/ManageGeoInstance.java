package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManageGeoInstance extends com.sportservlet.html.ManageGeoInstance {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
