package com.sportservlet.ajax;

import com.sports.entity.*;
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
import java.util.List;

public class Standing extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int pid = getIntValuedParameterValue(req, "pid");
        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(compSeasonKey, pid);

        DbCalculation dbCalculation = new DbCalculation(stat);
        List<? extends Participant> standing = dbCalculation.getParticipantStandingCompSeasonPhase(cspk);
        CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> factory = dbCalculation.getCompSeasonParticipantFactory(competitionId);

        Writer w = resp.getWriter();
        w.append("<tr><th>Rank</th><th>");
        w.append(factory.getParticipantDescription());
        w.append("</th><th>Played</th><th>Points</th><th>Diff</th></tr>\n");

        for (Participant participant : standing) {
            w.append("<tr><td>");
            w.append(Integer.toString(participant.getRank()));
            w.append(".</td><td>");
            w.append(Util.convertEmptyString(participant.getDescription(), ""));
            w.append("</td><td>");
            w.append(Integer.toString(participant.getPlayed()));
            w.append("</td><td>");
            w.append(Integer.toString(participant.getPoints()));
            w.append("</td><td>");
            w.append(Integer.toString(participant.getScoreDiff()));
            w.append("</td></tr>\n");
        }
    }
}
