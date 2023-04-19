package com.management.servlet.ajax;

import com.sports.entity.CompSeason;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.CompSeasonKeyFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;
import java.time.LocalDateTime;

public class ProcessManageCompSeason extends ProcessManageSuperKeyEntity<CompSeasonKey, CompSeason> {
    @Override
    protected SuperKeySuperManager<CompSeasonKey, CompSeason> getSuperManager(Statement stat) {
        return new CompSeasonManager(stat);
    }

    @Override
    protected CompSeasonKey getNewSuperKey(SuperKeySuperManager<CompSeasonKey, CompSeason> superManager, HttpServletRequest req) {
        return null;
    }

    @Override
    protected String getUpdateIdStr(CompSeasonKey superKey) {
        return null;
    }

    @Override
    protected CompSeasonKey getSuperKeyFromRequest(HttpServletRequest req) {
        return compSeasonKey;
    }

    @Override
    protected CompSeason getNewEntity() {
        return null;
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        Integer tab = convertRequestParamToNonIdInteger(req, "tab");
        Integer trb = convertRequestParamToNonIdInteger(req, "trb");
        Integer ldb = convertRequestParamToNonIdInteger(req, "ldb");
        LocalDateTime sd = convertRequestParameterToDatetime(req, "sd");
        LocalDateTime ed = convertRequestParameterToDatetime(req, "ed");

        entity.setTriesAbsBonus(tab);
        entity.setTriesRelBonus(trb);
        entity.setLossDiffBonus(ldb);
        entity.setStartDate(sd);
        entity.setEndDate(ed);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new CompSeasonKeyFlusher(superKey);
    }
}
