package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.comparator.H2HMatchKnockoutOrderDate;
import com.sports.entity.key.*;
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
import java.util.List;
import java.util.Map;

public abstract class MatchList extends SuperResponseServlet {
    protected abstract String getMatchInfoLink();

    protected abstract String getMatchLiveLink();

    protected abstract String getManageMatchLink();

    abstract List<? extends H2HMatch> getMatchList(Statement stat, HttpServletRequest req,
                                                   CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                                                           ? extends CompSeasonPhaseParticipantKey,
                                                           ? extends Participant,
                                                           ? extends SuperKeyEntity,
                                                           ? extends H2HMatchKey,
                                                           ? extends H2HMatch,
                                                           ? extends H2HMatchPartKey,
                                                           ? extends H2HMatchPart,
                                                           ? extends H2HMatchPartStatKey,
                                                           ? extends H2HMatchPartStat> factory) throws SQLException;

    abstract void writeMatchRow(Statement stat, H2HMatch h2HMatch, Map<Integer, ? extends Participant> particMap, Writer w) throws SQLException, IOException;

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);

        List<? extends H2HMatch> h2hMatches = getMatchList(stat, req, factory);

        h2hMatches.sort(new H2HMatchKnockoutOrderDate());

        List<Integer> particIds = new ArrayList<>();

        for (H2HMatch match : h2hMatches) {
            if (match.getParticipant1Id() != null)
                particIds.add(match.getParticipant1Id());

            if (match.getParticipant2Id() != null)
                particIds.add(match.getParticipant2Id());
        }

        Map<Integer, ? extends Participant> particMap = factory.getParticipantManager(stat).getParticipantMap(particIds);

        Writer w = resp.getWriter();

        for (H2HMatch match : h2hMatches)
            writeMatchRow(stat, match, particMap, w);
    }

    void writeMatchRowWithLinks(Statement stat, H2HMatch h2HMatch, Map<Integer, ? extends Participant> particMap, Writer w)
            throws SQLException, IOException {
        String line = "<tr>" + getMatchParticipantInfo(h2HMatch, particMap) + "<td>";

        if (showMatchLiveLink(h2HMatch))
            line += getAnchor(h2HMatch, getMatchLiveLink(), "Live Match", null);
        else
            line += getScoreString(stat, h2HMatch);

        line += "</td><td>" + Util.convertEmptyDateTimeToDateString(h2HMatch.getDate()) + "</td><td>";
        line += getAnchor(h2HMatch, getMatchInfoLink(), "Match Info", null) + "</td><td>";
        line += getAnchor(h2HMatch, getManageMatchLink(), "Manage Match", "md=u");
        line += "</td></tr>\n";

        w.append(line);
    }

    protected boolean showMatchLiveLink(H2HMatch match) {
        return !match.isFinished();
    }

    String getMatchParticipantInfo(H2HMatch h2HMatch, Map<Integer, ? extends Participant> particMap) {
        return getTd(h2HMatch.getParticipant1Id(), particMap,
                h2HMatch.isFinished() && h2HMatch.isParticipant1Win()) +
                getTd(h2HMatch.getParticipant2Id(), particMap, h2HMatch.isParticipant2Win());
    }

    String getAnchor(H2HMatch match, String link, String value, String extraParams) {
        return getAnchor(match, link, value, extraParams, "mid");
    }

    String getAnchor(H2HMatch match, String link, String value, String extraParams, String paramSpecificId) {
        return "<a href=\"" + path + "/" + link + "?" + compSeasonUrlParameters +
                "&pid=" + match.getCompSeasonPhaseId() + "&" + paramSpecificId + "=" + match.getSpecificId() +
                Util.getPrefixedStringOrEmptyString(extraParams, "&") +"\">" +
                value + "</a>";
    }

    private String getTd(Integer particId, Map<Integer, ? extends Participant> particMap, boolean isWinner) {
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
