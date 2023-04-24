package com.sportservlet.ajax;

import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.SuperKeySuperManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class ProcessManageCompSeasonEventPart extends ProcessManageSuperKeyEntity<CompSeasonEventPartKey, CompSeasonEventPart> {
    private CompSeasonEventKey csek;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {
        csek = getCompSeasonEventKey(req);
    }

    protected SuperKeySuperManager<CompSeasonEventPartKey, CompSeasonEventPart> getSuperManager(Statement stat) {
        return new CompSeasonEventPartManager(stat);
    }

    protected CompSeasonEventPartKey getNewSuperKey(SuperKeySuperManager<CompSeasonEventPartKey, CompSeasonEventPart> superManager, HttpServletRequest req) throws SQLException {
        int csepid = ((CompSeasonEventPartManager)superManager).getNewCompSeasonEventPartId(csek);
        return new CompSeasonEventPartKey(csek, csepid);
    }

    protected String getUpdateIdStr(CompSeasonEventPartKey superKey) {
        return String.valueOf(superKey.getCompSeasonEventPartId());
    }

    protected CompSeasonEventPartKey getSuperKeyFromRequest(HttpServletRequest req) {
        return new CompSeasonEventPartKey(csek, getIntValuedParameterValue(req, "csepid"));
    }

    protected CompSeasonEventPart getNewEntity() {
        return new CompSeasonEventPart();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        Integer epid = convertRequestParamToIdInteger(req, "epid");
        Integer did = convertRequestParamToIdInteger(req, "did");
        Integer epnid = convertRequestParamToIdInteger(req, "epnid");
        int o = getIntValuedParameterValue(req, "o");
        Integer st = convertRequestParamToNonIdInteger(req, "st");
        LocalDateTime dt = convertRequestParameterToDatetime(req, "dt");
        String es = req.getParameter("es");

        entity.setSportEventPartId(epid);
        entity.setSportDisciplineId(did);
        entity.setEventPartNameId(epnid);
        entity.setOrder(o);
        entity.setStage(st);
        entity.setDate(dt);
        entity.setExternalSource(es);
    }
}
