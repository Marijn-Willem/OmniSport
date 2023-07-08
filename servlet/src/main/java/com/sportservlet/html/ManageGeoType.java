package com.sportservlet.html;

import com.sports.entity.GeoType;
import com.sports.entity.manager.GeoTypeManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageGeoType extends ManageEntity {
    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("geotype");
    }

    @Override
    protected String getEntityIdName() {
        return "gtid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        GeoType geoType = null;

        if (!"i".equals(mode)) {
            int gtid = getIntValuedParameterValue(req, "gtid");
            geoType = new GeoTypeManager(stat).getEntityFromId(gtid);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", geoType != null ? geoType.getName() : null, w);
    }
}
