package com.cyclingroad.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class PersonSearch extends com.sportservlet.html.PersonSearch {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "Welcome";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
