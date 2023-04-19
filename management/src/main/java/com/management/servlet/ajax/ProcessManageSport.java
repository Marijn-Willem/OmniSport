package com.management.servlet.ajax;

import com.sports.entity.Sport;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.SportManager;
import com.sportservlet.ajax.ProcessManageIntEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.SportFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageSport extends ProcessManageIntEntity<Sport> {
    protected Sport getNewEntity() {
        return new Sport();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        boolean it = Boolean.parseBoolean(req.getParameter("it"));
        boolean h2h = Boolean.parseBoolean(req.getParameter("h2h"));
        boolean hmp = Boolean.parseBoolean(req.getParameter("hmp"));

        entity.setName(nm);
        entity.setTeam(it);
        entity.setH2H(h2h);
        entity.setHasMatchParts(hmp);
    }

    protected IntSuperManager<Sport> getSuperManager(Statement stat) {
        return new SportManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "spid");
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new SportFlusher(id);
    }
}
