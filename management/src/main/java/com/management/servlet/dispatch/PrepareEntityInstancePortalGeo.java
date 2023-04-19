package com.management.servlet.dispatch;

import com.sports.entity.Geo;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.dispatch.SuperDispatchServlet;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class PrepareEntityInstancePortalGeo extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        Geo geo = new DbCalculation(stat).getGeoFromOutputString(req.getParameter("nm"));

        if (geo != null) {
            req.setAttribute("eid", geo.getId());
            dispatchURL = "EntityInstancePortal";
        }
        else
            dispatchURL = "GeoPortal";
    }
}
