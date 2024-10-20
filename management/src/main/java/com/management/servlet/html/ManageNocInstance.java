package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManageNocInstance extends com.sportservlet.html.ManageNocInstance {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
