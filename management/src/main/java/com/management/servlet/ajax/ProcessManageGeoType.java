package com.management.servlet.ajax;

import com.sports.entity.GeoType;
import com.sports.entity.manager.GeoTypeManager;
import com.sports.entity.manager.IntSuperManager;
import com.sports.logic.util.Util;
import com.sportservlet.ajax.ProcessManageIntEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.GeoTypeFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageGeoType extends ProcessManageIntEntity<GeoType> {
    protected IntSuperManager<GeoType> getSuperManager(Statement stat) {
        return new GeoTypeManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return Util.convertStringToInteger(req.getParameter("gtid"));
    }

    protected GeoType getNewEntity() {
        return new GeoType();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        entity.setName(nm);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new GeoTypeFlusher(id);
    }
}
