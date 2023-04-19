package com.sportservlet.ajax;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class MatchMatrix extends SuperResponseServlet {
    protected CompSeasonPhaseKey cspk;

    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        preProcess(stat);

        int phaseId = Integer.parseInt(req.getParameter("pid"));
        boolean oc = Boolean.parseBoolean(req.getParameter("oc"));

        cspk = new CompSeasonPhaseKey(compSeasonKey, phaseId);

        CompSeasonParticipantFactory factory = new com.sports.logic.calculation.DbCalculation(stat)
                .getCompSeasonParticipantFactory(competitionId);

        CompSeasonPhaseParticipantManager csppm = factory.getPhaseParticManager(stat);
        ParticipantManager<? extends Participant> pm = factory.getParticipantManager(stat);

        List<Integer> particIds = csppm.getParticipantsInCompSeasonPhases(Collections.singletonList(cspk));
        List<? extends Participant> participants = pm.getParticipantList(particIds);
        sortParticipants(stat, participants);

        List<List<List<H2HMatch>>> matchMatrix = new DbCalculation(stat).getMatchMatrixCompSeasonPhase(cspk, participants);

        Writer w = resp.getWriter();

        w.append("<tr><th/>");

        for (int i = 0; i < participants.size(); i++)
            writeColumnHeader(participants.get(i), i + 1, w);

        w.append("</tr>\n");

        for (int i = 0; i < participants.size(); i++) {
            Participant partic = participants.get(i);
            writeRow(partic, matchMatrix.get(i), oc, w);
        }
    }

    protected void preProcess(Statement stat) throws SQLException {}

    protected String getOnClick(int mId) {
        return "";
    }

    protected <S extends Participant> void sortParticipants(Statement stat, List<S> participants) throws SQLException {
        participants.sort(new DescribedEntityDescription());
    }

    protected void writeColumnHeader(Participant partic, int colIndX, Writer w) throws IOException {
        w.append("<th colindx=\"");
        w.append(Integer.toString(colIndX));
        w.append("\">");
        w.append(partic.getDescription());
        w.append("</th>");
    }

    protected void writeRowHeader(Participant partic, int rowSpan, Writer w) throws IOException {
        w.append("<th rowspan=\"");
        w.append(Integer.toString(rowSpan));
        w.append("\">");
        w.append(partic.getDescription());
        w.append("</th>");
    }

    private void writeRow(Participant partic, List<List<H2HMatch>> matrixRow, boolean oc, Writer w) throws IOException {
        int rowSpan = getRowSpanRow(matrixRow);

        int[] colRowDp = new int[matrixRow.size()];
        int[] colMatchDp = new int[matrixRow.size()];

        for (int i = 0; i < rowSpan; i++) {
            w.append("<tr>");
            if (i == 0)
                writeRowHeader(partic, rowSpan, w);

            for (int j = 0; j < matrixRow.size(); j++) {
                List<H2HMatch> matches = matrixRow.get(j);

                if (i == 0 && matches.size() == 0) {
                    w.append("<td rowspan=\"");
                    w.append(Integer.toString(rowSpan));
                    w.append("\" colindx=\"");
                    w.append(Integer.toString(j + 1));
                    w.append("\">-</td>");
                }
                else if (i == colRowDp[j]) {
                    int matchIndX = colMatchDp[j];
                    int rowSpanCell = getRowSpanCell(matches.size(), matchIndX, rowSpan);

                    H2HMatch match = matches.get(matchIndX);
                    String score = Util.concatStringsWithDelimiter(
                            Util.convertIntegerToString(match.getScore1_1()),
                            Util.convertIntegerToString(match.getScore1_2()),
                            " - ");

                    w.append("<td rowspan=\"");
                    w.append(Integer.toString(rowSpanCell));
                    w.append("\" colindx=\"");
                    w.append(Integer.toString(j + 1));
                    w.append("\"");
                    if (oc) {
                        String onClick = getOnClick(match.getSpecificId());
                        w.append(onClick);
                    }
                    w.append(">");
                    w.append(score);
                    w.append("</td>");

                    colRowDp[j] += rowSpanCell;
                    colMatchDp[j] += 1;
                }
            }

            w.append("</tr>\n");
        }
    }

    private int getRowSpanRow(List<List<H2HMatch>> matrixRow) {
        int rowSpan = 1;

        for (List<H2HMatch> matches : matrixRow)
            rowSpan = Math.max(rowSpan, matches.size());

        return rowSpan;
    }

    private int getRowSpanCell(int nrMatches, int matchIndX, int rowSpanRow) {
        int minSpan = rowSpanRow / nrMatches;
        int addition = matchIndX < rowSpanRow % nrMatches ? 1 : 0;

        return minSpan + addition;
    }
}
