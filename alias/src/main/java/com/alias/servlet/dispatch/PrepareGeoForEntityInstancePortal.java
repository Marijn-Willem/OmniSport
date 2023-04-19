package com.alias.servlet.dispatch;

import com.sports.entity.Geo;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.dispatch.SuperDispatchServlet;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class PrepareGeoForEntityInstancePortal extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        String nm = req.getParameter("nm");
        Geo geo = new DbCalculation(stat).getGeoFromOutputString(nm);

        if (geo != null) {
            req.setAttribute("eid", geo.getId());
            dispatchURL = "EntityInstancePortal";
        }
        else
            dispatchURL = "GeoPortal";
    }
}
