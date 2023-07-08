package com.h2hsports.servlet.dispatch;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.H2HMatch;
import com.sports.entity.H2HMatchPart;
import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.StatType;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.H2HMatchPartManager;
import com.sports.entity.manager.H2HMatchPartStatManager;
import com.sports.entity.manager.NoCountResultManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;
import com.sports.logic.factory.H2HPartObjectFactory;
import com.sports.logic.factory.H2HPartStatObjectFactory;
import com.sportservlet.dispatch.SuperDispatchServlet;
import com.sportservlet.flush.H2HMatchFlusher;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class ProcessAddMatchScore extends SuperDispatchServlet {
    private CompSeasonParticipantFactory factory;
    private H2HObjectFactory h2HObjectFactory;
    private H2HMatchKey h2hMatchKey;
    private H2HMatch h2hMatch;

    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        int h2hMatchId = getIntValuedParameterValue(req, "mid");

        dispatchURL = "MatchOverview?" + compSeasonUrlParameters;

        factory = new com.sports.logic.calculation.DbCalculation(stat).
                getCompSeasonParticipantFactory(competitionId);
        h2HObjectFactory = factory.getH2HObjectFactory();

        h2hMatchKey = h2HObjectFactory.getKey(compSeasonKey, h2hMatchId);

        H2HMatchManager mm = h2HObjectFactory.getManager(stat);
        h2hMatch = mm.getInstanceFromKey(h2hMatchKey);

        h2hMatch.setParticipant1Id(getIntValuedParameterValue(req, "p1id"));
        h2hMatch.setParticipant2Id(getIntValuedParameterValue(req, "p2id"));

        Map<String, Integer> noCountResultMap = new NoCountResultManager(stat).getNameIdMap();

        if (noCountResultMap.containsKey(req.getParameter("ncr_1")))
            h2hMatch.setParticipant1NcrId(noCountResultMap.get(req.getParameter("ncr_1")));

        if (noCountResultMap.containsKey(req.getParameter("ncr_2")))
            h2hMatch.setParticipant2NcrId(noCountResultMap.get(req.getParameter("ncr_2")));

        mm.update(h2hMatchKey, h2hMatch);

        if (req.getParameter("scr_m_1") != null)
            processMatchScores(req);

        processSetScores(stat, req);

        if (req.getParameter("scr_m_1") != null ||
                req.getParameter("ncr_1") != null ||
                req.getParameter("ncr_2") != null)
            new DbCalculation(stat).processFinishH2HMatch(factory, h2hMatchKey, h2hMatch);

        cacheFlusher = new H2HMatchFlusher(h2hMatchKey);
    }

    private void processMatchScores(HttpServletRequest req) {
        int matchScore1 = getIntValuedParameterValue(req, "scr_m_1");
        int matchScore2 = getIntValuedParameterValue(req, "scr_m_2");

        h2hMatch.setScore1_1(matchScore1);
        h2hMatch.setScore1_2(matchScore2);
    }

    private void processSetScores(Statement stat, HttpServletRequest req) throws SQLException {
        H2HPartObjectFactory partFactory = h2HObjectFactory.getPartObjectFactory();
        H2HPartStatObjectFactory partStatFactory = partFactory.getPartStatObjectFactory();

        H2HMatchPartManager mpm = partFactory.getMatchPartManager(stat);
        H2HMatchPartStatManager mpsm = null;

        int setNr = 0;

        while (req.getParameter("scr_s" + ++setNr + "_1") != null) {
            int setScore1 = Integer.parseInt(req.getParameter("scr_s" + setNr + "_1"));
            int setScore2 = Integer.parseInt(req.getParameter("scr_s" + setNr + "_2"));

            H2HMatchPartKey h2hMatchPartKey = partFactory.getMatchPartKey(h2hMatchKey, setNr);
            H2HMatchPart h2hMatchPart = partFactory.getMatchPart();

            h2hMatchPart.setName("Set " + setNr);
            h2hMatchPart.setParticipant1Win(setScore1 > setScore2);
            h2hMatchPart.setFinished(true);

            mpm.insert(h2hMatchPartKey, h2hMatchPart);

            H2HMatchPartStatKey mpsk1 = partStatFactory.getMatchPartStatKey(h2hMatchPartKey, 1);
            H2HMatchPartStatKey mpsk2 = partStatFactory.getMatchPartStatKey(h2hMatchPartKey, 2);

            H2HMatchPartStat mps1 = partStatFactory.getMatchPartStat();
            mps1.setParticipantId(h2hMatch.getParticipant1Id());
            mps1.setStatTypeId(StatType.statTypeScoreId);
            mps1.setValue(setScore1);

            H2HMatchPartStat mps2 = partStatFactory.getMatchPartStat();
            mps2.setParticipantId(h2hMatch.getParticipant2Id());
            mps2.setStatTypeId(StatType.statTypeScoreId);
            mps2.setValue(setScore2);

            if (setNr == 1)
                mpsm = partFactory.getPartStatObjectFactory().getStatManager(stat);

            mpsm.insert(mpsk1, mps1);
            mpsm.insert(mpsk2, mps2);
        }
    }
}
