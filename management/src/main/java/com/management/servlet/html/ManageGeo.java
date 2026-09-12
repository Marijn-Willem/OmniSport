package com.management.servlet.html;

import com.sports.entity.Geo;
import com.sports.entity.GeoType;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.GeoManager;
import com.sports.entity.manager.GeoTypeManager;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ManageGeo extends ManageEntity {
    private DbCalculation dbCalc;
    private Geo geo;

    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "GeoPortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("geo");
        jsSpecificList.add("geo");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return null;
    }

    @Override
    protected void preProcessSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        dbCalc = new DbCalculation(stat);
        String nm = req.getParameter("nm");
        geo = nm != null ? dbCalc.getGeoFromOutputString(nm) : null;
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        GeoManager gm = new GeoManager(stat);

        List<GeoType> geoTypes = new GeoTypeManager(stat).getAllGeoTypes();
        geoTypes.sort(new NamedEntityName());

        LinkedHashMap<Integer, String> geoTypeMap = new LinkedHashMap<>();

        for (GeoType geoType : geoTypes)
            geoTypeMap.put(geoType.getId(), geoType.getName());

        Geo parentGeo = geo != null && geo.getParentGeoId() != null ? gm.getEntityFromId(geo.getParentGeoId()) : null;
        if (parentGeo != null)
            dbCalc.setGeoOutputStrings(Collections.singletonList(parentGeo));

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", geo != null ? geo.getName() : null, w);
        writeSelectWithLabel("GeoType", "gtid", geoTypeMap, geo != null ? geo.getGeoTypeId() : null, w);
        writeTextFieldWithOnKeypress("Parent Geo", "pgn", parentGeo != null ? parentGeo.getOutputString() : null,
                "handleChangePgn()", w);
        w.append("<table id=\"tblGn\" border=\"1\">\n</table>\n");
        writeGeoSpatialField(geo != null ? geo.getCoordinates() : null, w);
    }

    protected String getUpdateId(HttpServletRequest req) {
        return geo != null ? String.valueOf(geo.getId()) : "";
    }
}
