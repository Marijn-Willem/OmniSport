package com.sportservlet.ajax;

import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.comparator.H2HMatchKnockoutOrder;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class MatchesCompSeasonPhase extends SuperResponseServlet {
    protected abstract String getMatchInfoLink();

    protected abstract String getMatchLiveLink();

    protected abstract String getManageMatchLink();

    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int compSeasonPhaseId = Integer.parseInt(req.getParameter("pid"));

        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(compSeasonKey, compSeasonPhaseId);

        CompSeasonParticipantFactory factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);
        H2HMatchManager h2HMatchManager = factory.getH2HObjectFactory().getManager(stat);

        List<H2HMatch> h2hMatches =
                h2HMatchManager.getH2HMatchesFromCompSeasonPhases(Collections.singletonList(cspk));

        h2hMatches.sort(new H2HMatchKnockoutOrder());

        List<Integer> particIds = new ArrayList<Integer>();

        for (H2HMatch match : h2hMatches) {
            if (match.getParticipant1Id() != null)
                particIds.add(match.getParticipant1Id());

            if (match.getParticipant2Id() != null)
                particIds.add(match.getParticipant2Id());
        }

        Map<Integer, Participant> particMap = factory.getParticipantManager(stat).getParticipantMap(particIds);

        Writer w = resp.getWriter();

        for (H2HMatch match : h2hMatches) {
            String line = "<tr>" + getTd(match.getParticipant1Id(), particMap,
                    match.isFinished() && match.isParticipant1Win()) +
                    getTd(match.getParticipant2Id(), particMap, match.isParticipant2Win()) + "<td>";

            if (showMatchLiveLink(match))
                line += getAnchor(match, getMatchLiveLink(), "Live Match", null);
            else
                line += getScoreString(stat, match);

            line += "</td><td>" + Util.convertEmptyDateTimeToDateString(match.getDate()) + "</td><td>";
            line += getAnchor(match, getMatchInfoLink(), "Match Info", null) + "</td><td>";
            line += getAnchor(match, getManageMatchLink(), "Manage Match", "md=u");
            line += "</td></tr>\n";

            w.append(line);
        }
    }

    protected boolean showMatchLiveLink(H2HMatch match) {
        return !match.isFinished();
    }

    private String getAnchor(H2HMatch match, String link, String value, String extraParams) {
        return "<a href=\"" + path + "/" + link + "?" + compSeasonUrlParameters +
                "&pid=" + match.getCompSeasonPhaseId() + "&mid=" + match.getSpecificId() +
                Util.getPrefixedStringOrEmptyString(extraParams, "&") +"\">" +
                value + "</a>";
    }

    private String getTd(Integer particId, Map<Integer, Participant> particMap, boolean isWinner) {
        String text = particId != null ? particMap.get(particId).getDescription() : "-";

        return "<td" + (isWinner ? " class=\"matchWinner\"" : "") + ">" + text + "</td>";
    }

    private String getScoreString(Statement stat, H2HMatch h2HMatch) throws SQLException {
        if (h2HMatch.getParticipant1NcrId() != null)
            return getNoCountResultMap(stat).get(h2HMatch.getParticipant1NcrId());

        if (h2HMatch.getParticipant2NcrId() != null)
            return getNoCountResultMap(stat).get(h2HMatch.getParticipant2NcrId());

        return Util.concatStringsWithDelimiter(Util.convertIntegerToString(h2HMatch.getScore1_1()),
                Util.convertIntegerToString(h2HMatch.getScore1_2()), " - ");
    }
}
