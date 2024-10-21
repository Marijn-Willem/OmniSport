package com.sportservlet.ajax;

import com.sports.entity.GeoInstance;
import com.sports.entity.key.GeoInstanceKey;
import com.sports.logic.factory.GeoInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ProcessManageGeoInstance extends ProcessManageEntityInstance<GeoInstanceKey, GeoInstance> {
    @Override
    GeoInstanceFactory getEntityInstanceFactory() {
        return new GeoInstanceFactory();
    }

    @Override
    void processSpecificFields(Statement stat, GeoInstance entityInstance, HttpServletRequest req) { }
}
