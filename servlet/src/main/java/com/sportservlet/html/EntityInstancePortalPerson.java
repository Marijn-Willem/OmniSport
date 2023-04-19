package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class EntityInstancePortalPerson extends EntityInstancePortal {
    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "PersonSearch";
    }
}
