package com.darts.servlet.dispatch;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.PersonMatch;
import com.sports.entity.PersonMatchPart;
import com.sports.entity.PersonMatchPartStat;
import com.sports.entity.StatType;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.key.PersonMatchPartStatKey;
import com.sports.entity.manager.PersonMatchPartManager;
import com.sports.entity.manager.PersonMatchPartStatManager;
import com.sports.logic.factory.CompSeasonPersonSportFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessLeg extends SuperDispatchServlet {
    private PersonMatchPartManager pmpm;

    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        dispatchURL = "LiveMatch";

        pmpm = new PersonMatchPartManager(stat);
        PersonMatchPartStatManager pmpsm = new PersonMatchPartStatManager(stat);

        boolean p1win = Boolean.parseBoolean(req.getParameter("p1w"));

        String[] score1Str = req.getParameter("p1sc").split("\\|");
        String[] score2Str = req.getParameter("p2sc").split("\\|");

        int s1misDub = Integer.parseInt(req.getParameter("p1md"));
        int s2misDub = Integer.parseInt(req.getParameter("p2md"));

        PersonMatchKey personMatchKey = (PersonMatchKey)req.getSession().getAttribute("pmk");
        PersonMatch personMatch = (PersonMatch)req.getSession().getAttribute("pm");

        int personMatchPartId;
        PersonMatchPartKey pmpk;
        PersonMatchPart pmp;

        if (personMatch.getScore2Person1() == 0 && personMatch.getScore2Person2() == 0) {
            // New set
            int nrSets = personMatch.getPerson1score() + personMatch.getPerson2score();
            personMatchPartId = personMatch.getMaxPersonMatchPart2Id() + 1;

            pmpk = new PersonMatchPartKey(personMatchKey, personMatchPartId);
            pmp = new PersonMatchPart();
            pmp.setName("Set " + (nrSets + 1));

            pmpm.insertPersonMatchPart(pmpk, pmp);

            personMatch.increaseMaxPersonMatchPart2Id();
            personMatch.setMaxPersonMatchPart1Id(personMatchPartId);
        }

        // Add new leg
        personMatchPartId = personMatch.getMaxPersonMatchPart2Id() + 1;
        int nrLegs = personMatch.getScore2Person1() + personMatch.getScore2Person2();
        pmpk = new PersonMatchPartKey(personMatchKey, personMatchPartId);

        pmp = new PersonMatchPart();
        pmp.setName("Leg " + (nrLegs + 1));
        pmp.setParentMatchPartId(personMatch.getMaxPersonMatchPart1Id());
        pmp.setPerson1Win(p1win);
        pmp.setFinished(true);

        pmpm.insertPersonMatchPart(pmpk, pmp);
        personMatch.increaseMaxPersonMatchPart2Id();

        // Insert throws
        PersonMatchPartStatKey pmpsk;
        PersonMatchPartStat pmps;

        for (int i = 0; i < score1Str.length; i++) {
            pmpsk = new PersonMatchPartStatKey(pmpk, i + 1);
            pmps = getPersonMatchPartStatThrow(score1Str[i], personMatch.getPersonSport1Id());

            pmpsm.insertPersonMatchPartStat(pmpsk, pmps);
        }

        for (int i = 0; i < score2Str.length; i++) {
            pmpsk = new PersonMatchPartStatKey(pmpk, score1Str.length + i + 1);
            pmps = getPersonMatchPartStatThrow(score2Str[i], personMatch.getPersonSport2Id());

            pmpsm.insertPersonMatchPartStat(pmpsk, pmps);
        }

        // Insert missed doubles
        pmpsk = new PersonMatchPartStatKey(pmpk, score1Str.length + score2Str.length + 1);
        pmps = getPersonMatchPartStatMisDub(s1misDub, personMatch.getPersonSport1Id());
        pmpsm.insertPersonMatchPartStat(pmpsk, pmps);

        pmpsk = new PersonMatchPartStatKey(pmpk, score1Str.length + score2Str.length + 2);
        pmps = getPersonMatchPartStatMisDub(s2misDub, personMatch.getPersonSport2Id());
        pmpsm.insertPersonMatchPartStat(pmpsk, pmps);

        // Handle score
        if (p1win)
            personMatch.increaseScore2Person1();
        else
            personMatch.increaseScore2Person2();

        if (endOfSet(personMatch))
            finishSet(stat, personMatch, personMatchKey);

        if (endOfMatch(personMatch))
            finishMatch(stat, personMatch, personMatchKey);
    }

    private PersonMatchPartStat getPersonMatchPartStatThrow(String scoreStr, int personSportId) {
        PersonMatchPartStat personMatchPartStat = new PersonMatchPartStat();
        personMatchPartStat.setStatTypeId(StatType.statTypeThrowId);
        personMatchPartStat.setPersonSportId(personSportId);

        String[] scoreStrSplit = scoreStr.split("_");
        personMatchPartStat.setValue(Integer.parseInt(scoreStrSplit[0]));
        personMatchPartStat.setValue2(Integer.parseInt(scoreStrSplit[1]));

        return personMatchPartStat;
    }

    private PersonMatchPartStat getPersonMatchPartStatMisDub(int misDub, int personSportId) {
        PersonMatchPartStat personMatchPartStat = new PersonMatchPartStat();

        personMatchPartStat.setStatTypeId(StatType.statTypeMisDubId);
        personMatchPartStat.setValue(misDub);
        personMatchPartStat.setPersonSportId(personSportId);

        return personMatchPartStat;
    }

    private boolean isDecider(PersonMatch personMatch) {
        return personMatch.getPerson1score() + personMatch.getPerson2score() == personMatch.getBestOf1() - 1;
    }

    private boolean endOfSet(PersonMatch personMatch) {
        int leadScore = Math.max(personMatch.getScore2Person1(), personMatch.getScore2Person2());
        int scoreDiff = Math.abs(personMatch.getScore2Person1() - personMatch.getScore2Person2());

        int bestOf2 = personMatch.getBestOf2();
        int bestOfDec = personMatch.getBestOfDec();

        return 2 * leadScore >= bestOf2 + 1 &&
                (!isDecider(personMatch) || scoreDiff >= 2 || 2 * leadScore == bestOfDec + 1);
    }

    private boolean endOfMatch(PersonMatch personMatch) {
        int leadScore = Math.max(personMatch.getPerson1score(), personMatch.getPerson2score());

        return 2 * leadScore == personMatch.getBestOf1() + 1;
    }

    private void finishSet(Statement stat, PersonMatch personMatch, PersonMatchKey pmk)
            throws SQLException {
        PersonMatchPartKey pmpk = new PersonMatchPartKey(pmk, personMatch.getMaxPersonMatchPart1Id());

        int gameScore1 = personMatch.getScore2Person1();
        int gameScore2 = personMatch.getScore2Person2();
        boolean person1win = gameScore1 > gameScore2;

        if (person1win)
            personMatch.increaseScore1Person1();
        else
            personMatch.increaseScore1Person2();

        personMatch.clearScores2();

        PersonMatchPart pmp = pmpm.getPersonMatchPart(pmpk);

        pmp.setPerson1Win(person1win);
        pmp.setFinished(true);
        pmpm.updatePersonMatchPart(pmpk, pmp);

        PersonMatchPartStat pmps1 = new PersonMatchPartStat();
        PersonMatchPartStat pmps2 = new PersonMatchPartStat();

        pmps1.setStatTypeId(StatType.statTypeScoreId);
        pmps1.setValue(gameScore1);
        pmps1.setPersonSportId(personMatch.getPersonSport1Id());

        pmps2.setStatTypeId(StatType.statTypeScoreId);
        pmps2.setValue(gameScore2);
        pmps2.setPersonSportId(personMatch.getPersonSport2Id());

        PersonMatchPartStatManager pmpsm = new PersonMatchPartStatManager(stat);

        pmpsm.insertPersonMatchPartStat(new PersonMatchPartStatKey(pmpk, 1), pmps1);
        pmpsm.insertPersonMatchPartStat(new PersonMatchPartStatKey(pmpk, 2), pmps2);
    }

    private void finishMatch(Statement stat, PersonMatch pm, PersonMatchKey pmk) throws SQLException {
        new DbCalculation(stat).processFinishH2HMatch(new CompSeasonPersonSportFactory(), pmk, pm);

        dispatchURL = "MatchStats";
    }
}
