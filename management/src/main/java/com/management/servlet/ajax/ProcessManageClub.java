package com.management.servlet.ajax;

import com.sports.entity.Club;
import com.sports.entity.manager.ClubManager;
import com.sports.entity.manager.IntSuperManager;
import com.sportservlet.ajax.ProcessManageIntEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageClub extends ProcessManageIntEntity<Club> {
    protected IntSuperManager<Club> getSuperManager(Statement stat) {
        return new ClubManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "clid");
    }

    protected Club getNewEntity() {
        return new Club();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        Integer cogeid = convertRequestParamToIdInteger(req, "cogeid");

        entity.setName(nm);
        entity.setCountryGeoId(cogeid);
    }
}
