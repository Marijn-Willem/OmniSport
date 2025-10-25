package com.h2hsports.servlet.html;

import com.sports.entity.*;
import com.sports.entity.comparator.H2HMatchPartId;
import com.sports.entity.comparator.H2HMatchPartStatId;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.H2HMatchPartManager;
import com.sports.entity.manager.H2HMatchPartStatManager;
import com.sports.entity.manager.NoCountResultManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;
import com.sports.logic.factory.H2HPartObjectFactory;
import com.sports.logic.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MatchInfo extends SuperHtmlServlet {
    private CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory;
    private H2HObjectFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> h2HObjectFactory;
    private H2HMatch h2hMatch;

    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        int h2hMatchId = Integer.parseInt(req.getParameter("mid"));

        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);
        factory = new com.sports.logic.calculation.DbCalculation(stat)
                .getCompSeasonParticipantFactory(competition);
        h2HObjectFactory = factory.getH2HObjectFactory();
        h2hMatch = new com.sports.calc.h2hsports.DbCalculation(stat).retrieveH2HMatch(h2HObjectFactory, compSeasonKey, h2hMatchId);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        String basicParameterString = compSeasonUrlParameters + "&pid=" + getIntValuedParameterValue(req, "pid");

        return h2hMatch.getParentMatchId() != null ?
                "ParentMatchPortal?" + basicParameterString + "&pmid=" + h2hMatch.getParentMatchId() :
                "MatchOverview?" + basicParameterString;
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        if (h2hMatch.isFinished() && h2hMatch.getParticipant1Id() != null && h2hMatch.getParticipant2Id() != null) {
            List<Integer> particIds = Arrays.asList(h2hMatch.getParticipant1Id(), h2hMatch.getParticipant2Id());

            H2HMatchKey h2hMatchKey = h2HObjectFactory.getKey(compSeasonKey, h2hMatch.getSpecificId());

            Map<Integer, ? extends Participant> particMap = factory.getParticipantManager(stat).getParticipantMap(particIds);
            w.append("<table border=\"1\">\n");

            String line = "<tr><th>" + particMap.get(h2hMatch.getParticipant1Id()).getDescription() +
                    "</th><th>Set</th><th>" +
                    particMap.get(h2hMatch.getParticipant2Id()).getDescription() + "</th></tr>\n";
            w.append(line);

            if (h2hMatch.getParticipant1NcrId() == null && h2hMatch.getParticipant2NcrId() == null)
                line = "<tr><td>" + Util.convertIntegerToString(h2hMatch.getScore1_1()) +
                        "</td><td>Match score</td><td>" +
                        Util.convertIntegerToString(h2hMatch.getScore1_2()) + "</td></tr>\n";
            else {
                Map<Integer, String> noCountResultMap = new NoCountResultManager(stat).getIdNameMap();

                line = "<tr><td>" +
                        Util.convertNullStringToEmpty(noCountResultMap.get(h2hMatch.getParticipant1NcrId())) +
                        "</td><td>NCR</td><td>" +
                        Util.convertNullStringToEmpty(noCountResultMap.get(h2hMatch.getParticipant2NcrId())) +
                        "</td></tr>\n";
            }

            w.append(line);

            H2HPartObjectFactory<? extends H2HMatchPartKey,
                    ? extends H2HMatchPart,
                    ? extends H2HMatchPartStatKey,
                    ? extends H2HMatchPartStat> partFactory = h2HObjectFactory.getPartObjectFactory();
            H2HMatchPartManager<? extends H2HMatchPartKey, ? extends H2HMatchPart> mpm = partFactory.getMatchPartManager(stat);
            List<? extends H2HMatchPart> h2hMatchParts = mpm.getH2HMatchPartsWithoutParent(h2hMatchKey);

            h2hMatchParts.sort(new H2HMatchPartId());

            List<H2HMatchPartKey> h2hMatchPartKeys = new ArrayList<>();

            for (H2HMatchPart h2hMatchPart : h2hMatchParts)
                h2hMatchPartKeys.add(partFactory.getMatchPartKey(h2hMatchKey, h2hMatchPart.getMatchPartId()));

            if (!h2hMatchParts.isEmpty()) {
                H2HMatchPartStatManager<? extends H2HMatchPartStatKey, ? extends H2HMatchPartStat> mpsm =
                        partFactory.getPartStatObjectFactory().getStatManager(stat);

                List<? extends H2HMatchPartStat> h2HMatchPartStats = mpsm.getH2HMatchPartStats(h2hMatchPartKeys,
                                Collections.singletonList(StatType.statTypeScoreId));
                h2HMatchPartStats.sort(new H2HMatchPartStatId());

                int points1;
                int points2;

                for (int i = 0; i < h2hMatchParts.size(); i++) {
                    points1 = h2HMatchPartStats.get(2 * i).getValue();
                    points2 = h2HMatchPartStats.get((2 * i) + 1).getValue();

                    line = "<tr><td>" + points1 + "</td><td>" + h2hMatchParts.get(i).getName() + "</td><td>" +
                            points2 + "</td></tr>\n";

                    w.append(line);
                }
            }

            w.append("</table>\n");
        }
        else
            w.append("<div>Match still in progress</div>\n");
    }
}
