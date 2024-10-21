package com.sportservlet.ajax;

import com.sports.entity.ClubInstance;
import com.sports.entity.Geo;
import com.sports.entity.key.ClubInstanceKey;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.ClubInstanceFactory;
import com.sports.logic.util.Util;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageClubInstance extends ProcessManageEntityInstance<ClubInstanceKey, ClubInstance> {
    @Override
    ClubInstanceFactory getEntityInstanceFactory() {
        return new ClubInstanceFactory();
    }

    @Override
    void processSpecificFields(Statement stat, ClubInstance entityInstance, HttpServletRequest req) throws SQLException {
        String cign = req.getParameter("cign");
        Geo cityGeo = !Util.isEmptyString(cign) ? new DbCalculation(stat).getGeoFromOutputString(cign) : null;

        entityInstance.setCityGeoId(cityGeo != null ? cityGeo.getId() : null);
    }
}
