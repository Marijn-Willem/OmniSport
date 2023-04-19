package com.management.servlet.ajax;

import com.sports.entity.Geo;
import com.sports.entity.manager.GeoManager;
import com.sports.entity.manager.IntSuperManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.util.Util;
import com.sportservlet.ajax.ProcessManageIntEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.GeoFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageGeo extends ProcessManageIntEntity<Geo> {
    protected IntSuperManager<Geo> getSuperManager(Statement stat) {
        return new GeoManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return Util.convertStringToInteger(req.getParameter("geid"));
    }

    protected Geo getNewEntity() {
        return new Geo();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        String nm = req.getParameter("nm");
        int gtid = getIntValuedParameterValue(req, "gtid");
        String pgn = req.getParameter("pgn");

        Geo parentGeo = !Util.isEmptyString(pgn) ? new DbCalculation(stat).getGeoFromOutputString(pgn) : null;

        entity.setName(nm);
        entity.setGeoTypeId(gtid);
        entity.setParentGeoId(parentGeo != null ? parentGeo.getId() : null);
        entity.setCoordinates(getPointFromParameter(req, "coo"));
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new GeoFlusher(entity.getId());
    }
}
