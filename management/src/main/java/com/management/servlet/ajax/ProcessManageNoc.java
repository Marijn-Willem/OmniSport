package com.management.servlet.ajax;

import com.sports.entity.Noc;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.NocManager;
import com.sportservlet.ajax.ProcessManageIntEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageNoc extends ProcessManageIntEntity<Noc> {
    @Override
    protected IntSuperManager<Noc> getSuperManager(Statement stat) {
        return new NocManager(stat);
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "nid");
    }

    @Override
    protected Noc getNewEntity() {
        return new Noc();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        Integer geid = convertRequestParamToIdInteger(req, "geid");

        entity.setName(nm);
        entity.setGeoId(geid);
    }
}
