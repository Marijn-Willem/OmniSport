package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class CompSeasonPortal extends com.sportservlet.html.CompSeasonPortal {
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "SportList";
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
