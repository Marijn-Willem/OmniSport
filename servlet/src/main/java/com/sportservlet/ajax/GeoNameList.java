package com.sportservlet.ajax;

import com.sports.entity.Geo;
import com.sports.entity.manager.GeoManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GeoNameList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String nm = req.getParameter("nm");

        GeoManager gm = new GeoManager(stat);
        List<Geo> geoList = gm.getGeosFromNameLike(nm);

        new DbCalculation(stat).setGeoOutputStrings(geoList);

        Writer w = resp.getWriter();

        for (Geo geo : geoList) {
            w.append("<tr coo=\"");
            w.append(Util.convertPointToString(geo.getCoordinates()));
            w.append("\" onclick=\"handleClickGeoName(this);\"><td>");
            w.append(geo.getOutputString());
            w.append("</td></tr>\n");
        }
    }
}
