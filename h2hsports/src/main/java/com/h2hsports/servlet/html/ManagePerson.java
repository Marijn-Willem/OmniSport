package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ManagePerson extends com.sportservlet.html.ManagePerson {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "PersonSearch";
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
