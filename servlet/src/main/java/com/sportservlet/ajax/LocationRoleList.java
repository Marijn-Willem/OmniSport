package com.sportservlet.ajax;

import com.sports.entity.LocationRole;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.LocationRoleManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class LocationRoleList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<LocationRole> locationRoles = new LocationRoleManager(stat).getAllLocationRoles();
        locationRoles.sort(new NamedEntityName());

        for (LocationRole locationRole : locationRoles)
            ServletUtil.writeOption(locationRole.getId(), locationRole.getName(), resp.getWriter());
    }
}
