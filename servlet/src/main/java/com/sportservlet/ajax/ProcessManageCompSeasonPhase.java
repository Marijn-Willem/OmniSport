package com.sportservlet.ajax;

import com.sports.entity.CompSeasonPhase;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.SuperKeySuperManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class ProcessManageCompSeasonPhase extends ProcessManageSuperKeyEntity<CompSeasonPhaseKey, CompSeasonPhase> {
    protected CompSeasonPhase getNewEntity() {
        return new CompSeasonPhase();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        Integer ppid = convertRequestParamToIdInteger(req, "ppid");
        Integer rn = convertRequestParamToNonIdInteger(req,"rn");
        Integer bo1 = convertRequestParamToNonIdInteger(req, "bo1");
        Integer bo2 = convertRequestParamToNonIdInteger(req, "bo2");
        Integer bod = convertRequestParamToNonIdInteger(req, "bod");
        boolean fn = Boolean.parseBoolean(req.getParameter("fn"));
        LocalDateTime sd = convertRequestParameterToDatetime(req, "sd");
        LocalDateTime ed = convertRequestParameterToDatetime(req, "ed");
        boolean st = Boolean.parseBoolean(req.getParameter("st"));
        boolean kop = Boolean.parseBoolean(req.getParameter("kop"));
        Integer po = convertRequestParamToNonIdInteger(req, "po");
        Integer ef = convertRequestParamToNonIdInteger(req, "ef");
        boolean ds = Boolean.parseBoolean(req.getParameter("ds"));
        int ptid = Integer.parseInt(req.getParameter("ptid"));
        Integer pmtid = convertRequestParamToIdInteger(req, "pmtid");

        entity.setParentPhaseId(ppid);
        entity.setRound(rn);
        entity.setBestOf1(bo1);
        entity.setBestOf2(bo2);
        entity.setBestOfDec(bod);
        entity.setFinished(fn);
        entity.setStartDate(sd);
        entity.setEndDate(ed);
        entity.setHasStanding(st);
        entity.setKnockoutParent(kop);
        entity.setParentOrder(po);
        entity.setExpandFactor(ef);
        entity.setHasDivisionStandings(ds);
        entity.setPhaseTypeId(ptid);
        entity.setParentMatchTypeId(pmtid);
    }

    protected SuperKeySuperManager<CompSeasonPhaseKey, CompSeasonPhase> getSuperManager(Statement stat) {
        return new CompSeasonPhaseManager(stat);
    }

    protected CompSeasonPhaseKey getNewSuperKey(SuperKeySuperManager<CompSeasonPhaseKey, CompSeasonPhase> superManager,
                                      HttpServletRequest req) throws SQLException{
        int phaseId = ((CompSeasonPhaseManager)superManager).getNewPhaseId(compSeasonKey);
        return new CompSeasonPhaseKey(compSeasonKey, phaseId);
    }

    protected String getUpdateIdStr(CompSeasonPhaseKey superKey) {
        return "" + superKey.getCompSeasonPhaseId();
    }

    protected CompSeasonPhaseKey getSuperKeyFromRequest(HttpServletRequest req) {
        Integer phaseId = convertRequestParamToIdInteger(req, "pid");

        return new CompSeasonPhaseKey(compSeasonKey, phaseId);
    }
}
