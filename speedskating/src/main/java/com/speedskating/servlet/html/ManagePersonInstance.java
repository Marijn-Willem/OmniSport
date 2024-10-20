package com.speedskating.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManagePersonInstance extends com.sportservlet.html.ManagePersonInstance {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
