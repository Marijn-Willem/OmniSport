package com.management.servlet.html;

import com.sports.entity.LocationRole;
import com.sports.entity.manager.LocationRoleManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageLocationRole extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "LocationRolePortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("locationrole");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "lrid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        LocationRole locationRole = null;

        if (!"i".equals(mode)) {
            int lrid = getIntValuedParameterValue(req, "lrid");
            locationRole = new LocationRoleManager(stat).getEntityFromId(lrid);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", locationRole != null ? locationRole.getName() : null, w);
    }
}
