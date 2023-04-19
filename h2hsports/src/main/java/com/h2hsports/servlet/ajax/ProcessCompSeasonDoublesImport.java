package com.h2hsports.servlet.ajax;

import com.sports.entity.CompSeasonPhase;
import com.sports.entity.Competition;
import com.sports.entity.DoublesMatch;
import com.sports.entity.Gender;
import com.sports.entity.comparator.H2HMatchPhaseRoundKnockoutOrder;
import com.sports.entity.key.CompSeasonDoubleKey;
import com.sports.entity.key.CompSeasonPhaseDoubleKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.DoublesMatchKey;
import com.sports.entity.manager.*;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ProcessCompSeasonDoublesImport extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonDoubleManager csdm = new CompSeasonDoubleManager(stat);

        List<Integer> compSeasDoubleIds = csdm.getCompSeasonDoubleIds(compSeasonKey);
        String[] nameArr = (String[])req.getSession().getAttribute("names");

        String errorMessage = null;

        if (compSeasDoubleIds.size() > 0)
            errorMessage = "Already doubles in this compseason!";
        else if (nameArr.length % 2 == 1)
            errorMessage = "Odd number of persons in memory!";

        if (errorMessage == null) {
            CompSeasonPhase phRound1 =
                    new CompSeasonPhaseManager(stat).getCompSeasonPhaseForRound(compSeasonKey, 1);

            CompSeasonPhaseDoubleManager cspdm = new CompSeasonPhaseDoubleManager(stat);

            List<String[]> namePairs = new ArrayList<>();

            for (int i = 0; i < nameArr.length / 2; i++) {
                String[] namePair = new String[] {
                        nameArr[2 * i],
                        nameArr[2 * i + 1]
                    };

                namePairs.add(namePair);
            }

            List<com.sports.entity.Double> doubles = new DbCalculation(stat).getDoubleListWithNewDoubles(namePairs, competitionId);

            List<CompSeasonDoubleKey> csdKeys = new ArrayList<>();
            List<CompSeasonPhaseDoubleKey> cspdKeys = new ArrayList<>();

            for (com.sports.entity.Double dbl : doubles) {
                csdKeys.add(new CompSeasonDoubleKey(compSeasonKey, dbl.getId()));

                if (phRound1 != null)
                    cspdKeys.add(new CompSeasonPhaseDoubleKey(phRound1.getCompSeasonPhaseKey(), dbl.getId()));
            }

            csdm.insertCompSeasonDoubleKeys(csdKeys);
            cspdm.insertPhaseParticipantKeyList(cspdKeys);

            if (phRound1 != null)
                addDoublesToFirstRoundMatches(stat, doubles, phRound1.getCompSeasonPhaseKey());

            Writer w = resp.getWriter();

            w.append("Doubles successfully imported");

            processNewDoubles(stat, doubles, w);

            req.removeAttribute("names");
        }
        else
            resp.getWriter().append(errorMessage);
    }

    private void addDoublesToFirstRoundMatches(Statement stat, List<com.sports.entity.Double> doubles,
                                               CompSeasonPhaseKey firstRoundKey) throws SQLException {
        DoublesMatchManager dmm = new DoublesMatchManager(stat);

        List<DoublesMatch> matchList = dmm.getMatchesInCompSeasonPhases(Collections.singletonList(firstRoundKey));

        matchList.sort(new H2HMatchPhaseRoundKnockoutOrder());

        for (int i = 0; i < Math.min(matchList.size(), doubles.size() / 2); i++) {
            DoublesMatch match = matchList.get(i);
            com.sports.entity.Double double1 = doubles.get(2 * i);
            com.sports.entity.Double double2 = doubles.get(2 * i + 1);

            match.setDouble1Id(double1.getId());
            match.setDouble2Id(double2.getId());
        }

        Map<DoublesMatchKey, DoublesMatch> matchMap = new HashMap<>() {{
            matchList.forEach(x -> {
                DoublesMatchKey matchKey = new DoublesMatchKey(firstRoundKey.getSuperKey(), x.getDoublesMatchId());
                put(matchKey, x);
            });
        }};

        dmm.updateMatchMap(matchMap);
    }

    private void processNewDoubles(Statement stat, List<com.sports.entity.Double> doubles, Writer w)
            throws SQLException, IOException {
        List<com.sports.entity.Double> newDoubles = new ArrayList<>();

        for (com.sports.entity.Double dbl : doubles)
            if (dbl.isNewlyCreated())
                newDoubles.add(dbl);

        if (newDoubles.size() > 0) {
            Competition comp = new CompetitionManager(stat).getCompetition(competitionId);

            w.append("<div>The following doubles are newly created,</div>\n");
            w.append("<div>between brackets the newly created persons</div>\n");

            if (comp.getGenderId() == Gender.genderIdMixed)
                w.append("<div>Pay attention: persons may be created with mixed gender id</div>\n");

            w.append("<br/>\n");

            for (com.sports.entity.Double newDbl : newDoubles) {
                List<String> newPersonNames = new ArrayList<>();

                if (newDbl.getPerson1().isNewlyCreated())
                    newPersonNames.add(newDbl.getPerson1().getName());

                if (newDbl.getPerson2().isNewlyCreated())
                    newPersonNames.add(newDbl.getPerson2().getName());

                String line = "<div>" + newDbl.getDescription() + " " + Util.getListBetweenBrackets(newPersonNames) +
                        "</div>\n";

                w.append(line);
            }
        }
    }
}
