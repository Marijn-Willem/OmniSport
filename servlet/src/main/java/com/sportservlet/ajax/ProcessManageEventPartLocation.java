package com.sportservlet.ajax;

import com.sports.db.type.Point;
import com.sports.entity.EventPartLocation;
import com.sports.entity.Geo;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartLocationKey;
import com.sports.entity.manager.EventPartLocationManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sports.logic.calculation.DbCalculation;

import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.EventPartLocationFlusher;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageEventPartLocation extends ProcessManageSuperKeyEntity<EventPartLocationKey, EventPartLocation> {
    private CompSeasonEventPartKey compSeasonEventPartKey;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {
        compSeasonEventPartKey = getCompSeasonEventPartKey(stat, req);
    }

    @Override
    protected EventPartLocationManager getSuperManager(Statement stat) {
        return new EventPartLocationManager(stat);
    }

    @Override
    protected EventPartLocationKey getNewSuperKey(SuperKeySuperManager<EventPartLocationKey, EventPartLocation> superManager,
                                                  HttpServletRequest req)
            throws SQLException {
        int eplid = ((EventPartLocationManager)superManager)
                .getNewEventPartLocationId(compSeasonEventPartKey);

        return new EventPartLocationKey(compSeasonEventPartKey, eplid);
    }

    @Override
    protected String getUpdateIdStr(EventPartLocationKey superKey) {
        return Integer.toString(superKey.getEventPartLocationId());
    }

    @Override
    protected EventPartLocationKey getSuperKeyFromRequest(HttpServletRequest req) {
        return new EventPartLocationKey(compSeasonEventPartKey, getIntValuedParameterValue(req, "eplid"));
    }

    @Override
    protected EventPartLocation getNewEntity() {
        return new EventPartLocation();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        String gn = req.getParameter("gn");
        Point coo = getPointFromParameter(req, "coo");
        int lrid = getIntValuedParameterValue(req, "lrid");

        Geo geo = new DbCalculation(stat).getGeoFromOutputString(gn);

        entity.setGeoId(geo != null ? geo.getId() : null);
        entity.setCoordinates(coo);
        entity.setLocationRoleId(lrid);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new EventPartLocationFlusher(superKey);
    }
}
