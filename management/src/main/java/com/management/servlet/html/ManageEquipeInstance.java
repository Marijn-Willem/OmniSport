package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManageEquipeInstance extends com.sportservlet.html.ManageEquipeInstance {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
