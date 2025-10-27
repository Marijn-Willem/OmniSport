package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.H2HMatchFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public abstract class ProcessManageH2HMatch<MK extends H2HMatchKey, M extends H2HMatch> extends ProcessManageSuperKeyEntity<MK, M> {
    private CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            MK,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory;

    private H2HObjectFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            MK,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> h2HObjectFactory;

    protected abstract CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            MK,
            M,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getFactory();

    protected SuperKeySuperManager<MK, M> getSuperManager(Statement stat) {
        return h2HObjectFactory.getManager(stat);
    }

    protected MK getNewSuperKey(SuperKeySuperManager<MK, M> superManager, HttpServletRequest req) throws SQLException {
        int matchId = ((H2HMatchManager<MK, M>)superManager).getNewSpecId(compSeasonKey);
        return h2HObjectFactory.getKey(compSeasonKey, matchId);
    }

    protected String getUpdateIdStr(MK superKey) {
        return "" + superKey.getSpecificId();
    }

    protected MK getSuperKeyFromRequest(HttpServletRequest req) {
        int matchId = Integer.parseInt(req.getParameter("mid"));
        return h2HObjectFactory.getKey(compSeasonKey, matchId);
    }

    protected M getNewEntity() {
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
        Integer pmid = convertRequestParamToIdInteger(req, "pmid");

        entity.setCompSeasonPhaseId(pid);
        entity.setCompSeasonPhaseKey(new CompSeasonPhaseKey(compSeasonKey, pid));
        entity.setParticipant1Id(p1id);
        entity.setParticipant2Id(p2id);
        entity.setScore1_1(p1s);
        entity.setScore1_2(p2s);
        entity.setDate(dt);
        entity.setParticipant1Start(p1st);
        entity.setParentMatchId(pmid);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new H2HMatchFlusher(superKey);
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {
        factory = getFactory();
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
