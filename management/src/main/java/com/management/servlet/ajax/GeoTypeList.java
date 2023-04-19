package com.management.servlet.ajax;

import com.sports.entity.GeoType;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.GeoTypeManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GeoTypeList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<GeoType> geoTypes = new GeoTypeManager(stat).getAllGeoTypes();
        geoTypes.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (GeoType geoType : geoTypes)
            ServletUtil.writeOption(geoType.getId(), geoType.getName(), w);
    }
}
