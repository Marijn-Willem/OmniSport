package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManagePerson extends com.sportservlet.html.ManagePerson {
    @Override
    protected String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "PersonSearch";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
