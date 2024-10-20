package com.sportservlet.ajax;

import com.sports.entity.ClubInstance;
import com.sports.entity.key.ClubInstanceKey;
import com.sports.logic.factory.ClubInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageClubInstance extends ProcessManageEntityInstance<ClubInstanceKey, ClubInstance> {
    @Override
    ClubInstanceFactory getEntityInstanceFactory() {
        return new ClubInstanceFactory();
    }

    @Override
    void processSpecificFields(ClubInstance entityInstance, HttpServletRequest req) {
        Integer cigeid = convertRequestParamToIdInteger(req, "cigeid");

        entityInstance.setCityGeoId(cigeid);
    }
}
