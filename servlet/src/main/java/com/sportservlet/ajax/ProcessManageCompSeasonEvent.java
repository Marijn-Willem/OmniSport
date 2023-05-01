package com.sportservlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SuperKeySuperManager;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageCompSeasonEvent extends ProcessManageSuperKeyEntity<CompSeasonEventKey, CompSeasonEvent> {
    @Override
    protected CompSeasonEvent getNewEntity() {
        return new CompSeasonEvent();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        int spid = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
        int seid = getIntValuedParameterValue(req, "seid");
        int gid = getIntValuedParameterValue(req, "gid");
        String es = req.getParameter("es");

        entity.setSportId(spid);
        entity.setSportEventId(seid);
        entity.setGenderId(gid);
        entity.setExternalSource(es);
    }

    @Override
    protected SuperKeySuperManager<CompSeasonEventKey, CompSeasonEvent> getSuperManager(Statement stat) {
        return new CompSeasonEventManager(stat);
    }

    @Override
    protected CompSeasonEventKey getNewSuperKey(SuperKeySuperManager<CompSeasonEventKey, CompSeasonEvent> superManager,
                                                HttpServletRequest req) throws SQLException {
        return ((CompSeasonEventManager)superManager).getNewCompSeasonEventKey(compSeasonKey);
    }

    @Override
    protected String getUpdateIdStr(CompSeasonEventKey superKey) {
        return Integer.toString(superKey.getCompSeasonEventId());
    }

    @Override
    protected CompSeasonEventKey getSuperKeyFromRequest(HttpServletRequest req) {
        int cseid = getIntValuedParameterValue(req, "cseid");

        return new CompSeasonEventKey(compSeasonKey, cseid);
    }

    @Override
    protected void postMortemSpecific(Statement stat) throws SQLException {
        if ("i".equals(mode))
            new DbCalculation(stat).postMortemInsertCompSeasonEvent(superKey, entity);
    }
}
