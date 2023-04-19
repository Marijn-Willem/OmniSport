package com.sportservlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.EventDisciplinePartManager;
import com.sports.entity.manager.SuperKeySuperManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageEventDisciplinePart extends ProcessManageSuperKeyEntity<EventDisciplinePartKey, EventDisciplinePart> {
    private CompSeasonEventPartKey csepKey;
    private SportDisciplineKey sdk;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {
        csepKey = getCompSeasonEventPartKey(stat, req);
        sdk = new DbCalculation(stat).getSportDisciplineKey(csepKey);
    }

    @Override
    protected SuperKeySuperManager<EventDisciplinePartKey, EventDisciplinePart> getSuperManager(Statement stat) {
        return new EventDisciplinePartManager(stat);
    }

    @Override
    protected EventDisciplinePartKey getNewSuperKey(SuperKeySuperManager<EventDisciplinePartKey, EventDisciplinePart> superManager, HttpServletRequest req) throws SQLException {
        int edpid = ((EventDisciplinePartManager)superManager).getNewEventDisciplinePartId(csepKey);
        return new EventDisciplinePartKey(csepKey, edpid);
    }

    @Override
    protected String getUpdateIdStr(EventDisciplinePartKey superKey) {
        return Integer.toString(superKey.getEventDisciplinePartId());
    }

    @Override
    protected EventDisciplinePartKey getSuperKeyFromRequest(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");
        return new EventDisciplinePartKey(csepKey, edpid);
    }

    @Override
    protected EventDisciplinePart getNewEntity() {
        return new EventDisciplinePart();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        entity.setSportDisciplineId(sdk.getSportDisciplineId());
        entity.setName(nm);
    }
}
