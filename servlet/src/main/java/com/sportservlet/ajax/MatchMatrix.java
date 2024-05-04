package com.sportservlet.ajax;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.*;
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

public abstract class MatchMatrix<P extends Participant> extends SuperResponseServlet {
    protected CompSeasonPhaseKey cspk;
    protected List<P> participants;
    protected CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            P,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory = getFactory();

    protected abstract CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            P,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getFactory();

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        int phaseId = Integer.parseInt(req.getParameter("pid"));
        cspk = new CompSeasonPhaseKey(compSeasonKey, phaseId);

        CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends SuperKeyEntity> csppm = factory.getPhaseParticManager(stat);
        ParticipantManager<P> pm = factory.getParticipantManager(stat);

        List<Integer> particIds = csppm.getParticipantsInCompSeasonPhases(Collections.singletonList(cspk));
        participants = pm.getParticipantList(particIds);
    }

    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        boolean oc = Boolean.parseBoolean(req.getParameter("oc"));

        sortParticipants();
        List<List<List<H2HMatch>>> matchMatrix = new DbCalculation(stat).getMatchMatrixCompSeasonPhase(cspk, participants);

        prepareData(matchMatrix);

        Writer w = resp.getWriter();

        writeColumnHeaders(w);

        for (int i = 0; i < participants.size(); i++) {
            Participant partic = participants.get(i);
            writeRow(partic, matchMatrix.get(i), oc, w);
        }
    }

    protected String getOnClick(int mId) {
        return "";
    }

    protected void sortParticipants() {
        participants.sort(new DescribedEntityDescription());
    }

    protected void prepareData(List<List<List<H2HMatch>>> matchMatrix) { }

    protected void writeColumnHeaders(Writer w) throws IOException {
        w.append("<tr><th/>");

        for (int i = 0; i < participants.size(); i++)
            writeColumnHeader(participants.get(i), i, w);

        w.append("</tr>\n");
    }

    protected void writeColumnHeader(Participant partic, int colIndX, Writer w) throws IOException {
        w.append("<th colindx=\"");
        w.append(Integer.toString(colIndX));
        w.append("\">");
        w.append(Util.convertNullStringToEmpty(partic.getDescription()));
        w.append("</th>");
    }

    protected void writeRowHeader(Participant partic, int rowSpan, Writer w) throws IOException {
        w.append("<th rowspan=\"");
        w.append(Integer.toString(rowSpan));
        w.append("\">");
        w.append(Util.convertNullStringToEmpty(partic.getDescription()));
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

                if (i == 0 && matches.isEmpty()) {
                    w.append("<td rowspan=\"");
                    w.append(Integer.toString(rowSpan));
                    w.append("\" colindx=\"");
                    w.append(Integer.toString(j));
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
                    w.append(Integer.toString(j));
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
