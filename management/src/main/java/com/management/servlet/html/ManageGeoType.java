package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageGeoType extends com.sportservlet.html.ManageGeoType {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "GeoTypePortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("geotype");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getUpdateId(HttpServletRequest req) {
        return convertRequestParameterToString(req, "gtid");
    }
}
