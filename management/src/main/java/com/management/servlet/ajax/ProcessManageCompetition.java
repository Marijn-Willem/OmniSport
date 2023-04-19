package com.management.servlet.ajax;

import com.sports.entity.Competition;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.IntSuperManager;
import com.sportservlet.ajax.ProcessManageIntEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.CompetitionFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;
import java.time.LocalDateTime;

public class ProcessManageCompetition extends ProcessManageIntEntity<Competition> {
    protected Competition getNewEntity() {
        return new Competition();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        int spid = Integer.parseInt(req.getParameter("spid"));
        int gid = Integer.parseInt(req.getParameter("gid"));
        boolean dbl = Boolean.parseBoolean(req.getParameter("dbl"));
        Integer cti = convertRequestParamToIdInteger(req, "cti");
        LocalDateTime cdi = convertRequestParameterToDatetime(req, "cdi");
        boolean dm = Boolean.parseBoolean(req.getParameter("dm"));
        Integer geid = convertRequestParamToIdInteger(req, "geid");

        entity.setName(nm);
        entity.setSportId(spid);
        entity.setGenderId(gid);
        entity.setH2hDouble(dbl);
        entity.setCupTeamInitId(cti);
        entity.setCupDateInit(cdi);
        entity.setDomestic(dm);
        entity.setGeoId(geid);
    }

    protected IntSuperManager<Competition> getSuperManager(Statement stat) {
        return new CompetitionManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return competitionId;
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new CompetitionFlusher(id);
    }
}
