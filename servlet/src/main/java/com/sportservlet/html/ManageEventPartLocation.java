package com.sportservlet.html;

import com.sports.entity.EventPartLocation;
import com.sports.entity.Geo;
import com.sports.entity.key.EventPartLocationKey;
import com.sports.entity.manager.EventPartLocationManager;
import com.sports.entity.manager.GeoManager;
import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public abstract class ManageEventPartLocation extends ManageEntity {
    @Override
    protected String getEntityIdName() {
        return "eplid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonEventPartVarsInScriptTag(req, w);
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("eventpartlocation");
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        EventPartLocation eventPartLocation = null;

        if (!"i".equals(mode)) {
            int eplid = getIntValuedParameterValue(req, "eplid");

            EventPartLocationKey eplKey = new EventPartLocationKey(getCompSeasonEventPartKey(req), eplid);

            eventPartLocation = new EventPartLocationManager(stat).getEntityFromSuperKey(eplKey);
        }

        Geo geo = eventPartLocation != null && eventPartLocation.getGeoId() != null ?
                new GeoManager(stat).getEntityFromId(eventPartLocation.getGeoId()) : null;

        if (geo != null)
            new DbCalculation(stat).setGeoOutputStrings(Collections.singletonList(geo));

        Writer w = res.getWriter();

        writeTextFieldWithOnKeypress("Geo", "gn", geo != null ? geo.getOutputString() : null, "handleChangeGn()", w);
        w.append("<table id=\"tblGn\" border=\"1\">\n</table>\n");
        writeGeoSpatialField("Coordinates", "coo", eventPartLocation != null ? eventPartLocation.getCoordinates() : null, w);
        writeSelectWithLabel("Location role", "lrid", getLocationRoleMap(stat),
                eventPartLocation != null ? eventPartLocation.getLocationRoleId() : null, w);
    }
}
