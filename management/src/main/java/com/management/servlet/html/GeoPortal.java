package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class GeoPortal extends SuperHtmlServlet {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("geo");
        jsList.add("geo");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<input id=\"gn\" type=\"text\" onkeypress=\"handleChangeGn();\" /><br/>\n");
        w.append("<table id=\"tblGn\" border=\"1\">\n</table>\n");
        w.append("<input type=\"button\" value=\"Manage Geo\" onclick=\"goToManageGeo();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Add Geo\" onclick=\"goToAddGeo();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Manage instances\" onclick=\"goToEntityInstancePortal();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"GeoType Portal\" onclick=\"goToGeoTypePortal();\" /><br/>\n");
    }
}
