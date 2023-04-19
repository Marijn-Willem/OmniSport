package com.sportservlet.ajax;

import com.sports.entity.H2HMatch;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.H2HMatchFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class ProcessManageH2HMatch extends ProcessManageSuperKeyEntity<H2HMatchKey, H2HMatch> {
    private CompSeasonParticipantFactory factory;
    private H2HObjectFactory h2HObjectFactory;

    protected SuperKeySuperManager<H2HMatchKey, H2HMatch> getSuperManager(Statement stat) {
        return h2HObjectFactory.getManager(stat);
    }

    protected H2HMatchKey getNewSuperKey(SuperKeySuperManager<H2HMatchKey, H2HMatch> superManager,
                               HttpServletRequest req) throws SQLException {
        int matchId = ((H2HMatchManager)superManager).getNewSpecId(compSeasonKey);
        return h2HObjectFactory.getKey(compSeasonKey, matchId);
    }

    protected String getUpdateIdStr(H2HMatchKey superKey) {
        return "" + superKey.getSpecificId();
    }

    protected H2HMatchKey getSuperKeyFromRequest(HttpServletRequest req) {
        int matchId = Integer.parseInt(req.getParameter("mid"));
        return h2HObjectFactory.getKey(compSeasonKey, matchId);
    }

    protected H2HMatch getNewEntity() {
        return h2HObjectFactory.getMatch();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        int pid = getIntValuedParameterValue(req, "pid");
        Integer p1id = convertRequestParamToIdInteger(req, "p1id");
        Integer p2id = convertRequestParamToIdInteger(req, "p2id");
        Integer p1s = convertRequestParamToNonIdInteger(req, "p1s");
        Integer p2s = convertRequestParamToNonIdInteger(req, "p2s");
        LocalDateTime dt = convertRequestParameterToDatetime(req, "dt");
        boolean p1st = Boolean.parseBoolean(req.getParameter("p1st"));

        entity.setCompSeasonPhaseId(pid);
        entity.setParticipant1Id(p1id);
        entity.setParticipant2Id(p2id);
        entity.setScore1_1(p1s);
        entity.setScore1_2(p2s);
        entity.setDate(dt);
        entity.setParticipant1Start(p1st);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new H2HMatchFlusher(superKey);
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {
        factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);
        h2HObjectFactory = factory.getH2HObjectFactory();
    }

    @Override
    protected void postMortemSpecific(Statement stat) throws SQLException {
        if (!entity.isFinished() && isMatchReadyToFinish())
            new com.sports.calc.h2hsports.DbCalculation(stat).processFinishH2HMatch(factory, superKey, entity);
    }

    private boolean isMatchReadyToFinish() {
        return entity.getParticipant1Id() != null && entity.getParticipant2Id() != null &&
                entity.getScore1_1() != null && entity.getScore1_2() != null;
    }
}
