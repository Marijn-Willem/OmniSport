package com.sportservlet.html;

import com.sports.entity.GeoInstance;
import com.sports.entity.key.GeoInstanceKey;
import com.sports.logic.factory.GeoInstanceFactory;

import java.io.Writer;
import java.sql.Statement;

public abstract class ManageGeoInstance extends ManageEntityInstance<GeoInstanceKey, GeoInstance> {
    @Override
    GeoInstanceFactory getFactory() {
        return new GeoInstanceFactory();
    }

    @Override
    String getServletNameReturnPath() {
        return "EntityInstancePortal";
    }

    @Override
    void writeSpecificFields(Statement stat, GeoInstance entityInstance, Writer w) { }
}
