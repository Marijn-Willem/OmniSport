package com.sportservlet.html;

import com.sports.entity.ClubInstance;
import com.sports.entity.Geo;
import com.sports.entity.key.ClubInstanceKey;
import com.sports.entity.manager.GeoManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.ClubInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public abstract class ManageClubInstance extends ManageEntityInstance<ClubInstanceKey, ClubInstance> {
    @Override
    ClubInstanceFactory getFactory() {
        return new ClubInstanceFactory();
    }

    @Override
    String getServletNameReturnPath() {
        return "EntityInstancePortal";
    }

    @Override
    void writeSpecificFields(Statement stat, ClubInstance entityInstance, Writer w) throws SQLException, IOException {
        Geo cityGeo = entityInstance.getCityGeoId() != null ?
                new GeoManager(stat).getEntityFromId(entityInstance.getCityGeoId()) : null;

        if (cityGeo != null)
            new DbCalculation(stat).setGeoOutputStrings(Collections.singletonList(cityGeo));

        writeTextFieldWithOnKeypress("City Geo", "cgn", cityGeo != null ? cityGeo.getOutputString() : null,
                "handleChangeCgn()", w);
        w.append("<table id=\"tblGn\" border=\"1\">\n</table>\n");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("geo");
    }
}
