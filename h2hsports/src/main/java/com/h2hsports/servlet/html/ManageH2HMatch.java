package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageH2HMatch extends com.sportservlet.html.ManageH2HMatch {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return getRegularReturnPath();
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
