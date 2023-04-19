package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class EntityInstancePortal extends com.sportservlet.html.EntityInstancePortal {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return req.getParameter("enm") + "Portal";
    }
}
