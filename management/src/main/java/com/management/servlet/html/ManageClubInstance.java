package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManageClubInstance extends com.sportservlet.html.ManageClubInstance {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
