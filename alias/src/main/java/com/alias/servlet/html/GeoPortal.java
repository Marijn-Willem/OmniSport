package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class GeoPortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<input id=\"gn\" type=\"text\" onkeypress=\"loadGeoNameList();\" /><br/>\n");
        w.append("<table id=\"tbl_gn\"></table><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToPrepareGeo();\" value=\"Manage aliases\" /><br/>\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }
}
