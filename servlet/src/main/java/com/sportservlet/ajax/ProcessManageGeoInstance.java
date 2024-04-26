package com.sportservlet.ajax;

import com.sports.entity.GeoInstance;
import com.sports.entity.key.GeoInstanceKey;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sports.logic.factory.GeoInstanceFactory;

public class ProcessManageGeoInstance extends ProcessManageEntityInstance<GeoInstanceKey, GeoInstance> {
    @Override
    EntityInstanceFactory<GeoInstanceKey, GeoInstance> getEntityInstanceFactory() {
        return new GeoInstanceFactory();
    }
}
