package com.management.servlet.ajax;

import com.sports.entity.PhaseType;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.PhaseTypeManager;
import com.sportservlet.ajax.ProcessManageIntEntity;

import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.PhaseTypeFlusher;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManagePhaseType extends ProcessManageIntEntity<PhaseType> {
    @Override
    protected IntSuperManager<PhaseType> getSuperManager(Statement stat) {
        return new PhaseTypeManager(stat);
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "ptid");
    }

    @Override
    protected PhaseType getNewEntity() {
        return new PhaseType();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        boolean ip = Boolean.parseBoolean(req.getParameter("ip"));
        Integer pptid = convertRequestParamToIdInteger(req, "pptid");

        entity.setName(nm);
        entity.setParent(ip);
        entity.setParentId(pptid);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new PhaseTypeFlusher(entity.getId());
    }
}
