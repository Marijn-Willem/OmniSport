package com.sportservlet.html;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SportManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;
import com.sports.logic.util.Util;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class ManageH2HMatch extends ManageEntity {
    private Competition competition;
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
            ? extends H2HMatchPartStat> h2hObjectFactory;

    private CompSeasonPhase compSeasonPhase;
    private Integer parentMatchId;
    private H2HMatch h2HMatch;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {
        competition = new CompetitionManager(stat).getCompetition(competitionId);
        factory = new com.sports.logic.calculation.DbCalculation(stat).getCompSeasonParticipantFactory(competition);
        h2hObjectFactory = factory.getH2HObjectFactory();

        h2HMatch = null;
        parentMatchId = Util.convertStringToInteger(req.getParameter("pmid"));

        DbCalculation dbCalc = new DbCalculation(stat);

        if (!"i".equals(mode)) {
            int matchId = Integer.parseInt(req.getParameter("mid"));

            h2HMatch = dbCalc.retrieveH2HMatch(h2hObjectFactory, compSeasonKey, matchId);
            parentMatchId = h2HMatch.getParentMatchId();
        }

        CompSeasonPhaseKey cspKey = new CompSeasonPhaseKey(compSeasonKey, getIntValuedParameterValue(req, "pid"));
        compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(cspKey);
        dbCalc.setPhaseDescriptionsFromTypes(Collections.singletonList(compSeasonPhase));
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("h2hmatch");
    }

    protected void writeSpecificFields(H2HMatch h2HMatch, Writer w) throws IOException { }

    @Override
    protected String getEntityIdName() {
        return "mid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("pid", getIntValuedParameterValue(req, "pid"), w);
        writeVarNameAndValue("pmid", parentMatchId, w);

        String dts = compSeasonPhase.getStartDate() != null ? Util.convertDateTimeToString(compSeasonPhase.getStartDate()) : null;
        String dte = compSeasonPhase.getEndDate() != null ? Util.convertDateTimeToString(compSeasonPhase.getEndDate()) : null;

        ServletUtil.writeStringConst("dts", dts, w);
        ServletUtil.writeStringConst("dte", dte, w);
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Sport sport = new SportManager(stat).getSport(competition.getSportId());
        String participantString = sport.isTeam() ? "Team" : competition.isH2hDouble() ? "Double" : "Person";

        List<CompSeasonPhase> compSeasonPhases = new CompSeasonPhaseManager(stat).getNonKnockoutCompSeasonPhases(compSeasonKey);
        compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());

        List<Integer> participantIds = getParticipantIds(stat);
        List<? extends Participant> participants = factory.getParticipantManager(stat).getParticipantList(participantIds);
        participants.sort(new DescribedEntityDescription());

        Writer w = res.getWriter();
        writeSpanWithLabel("Phase", compSeasonPhase.getDescription(), w);
        writeParticipantSelect(participants, participantString + " 1", "p1id",
                h2HMatch != null ? h2HMatch.getParticipant1Id() : null, w);
        writeParticipantSelect(participants, participantString + " 2", "p2id",
                h2HMatch != null ? h2HMatch.getParticipant2Id() : null, w);
        writeNumericTextField("Score " + participantString + " 1", "p1s", h2HMatch != null ? h2HMatch.getScore1_1() : null, w);
        writeNumericTextField("Score " + participantString + " 2", "p2s", h2HMatch != null ? h2HMatch.getScore1_2() : null, w);
        writeDateTimeField("Date", "dt", h2HMatch != null ?  h2HMatch.getDate() : null, w);
        writeCheckbox(participantString + " 1 Start", "p1st", h2HMatch != null && h2HMatch.isParticipant1Start(), w);
        writeCheckbox(participantString + " 1 Win", "p1w", h2HMatch != null && h2HMatch.isParticipant1Win(), true, w);
        writeCheckbox(participantString + " 2 Win", "p2w", h2HMatch != null && h2HMatch.isParticipant2Win(), true, w);
        writeSpecificFields(h2HMatch, w);
    }

    protected String getRegularReturnPath() {
        String basicParamString = compSeasonUrlParameters + "&pid=" + compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId();

        return parentMatchId != null ? "ParentMatchPortal?" + basicParamString + "&pmid=" + parentMatchId : "MatchOverview?" + basicParamString;
    }

    private List<Integer> getParticipantIds(Statement stat) throws SQLException {
        List<Integer> participantIds = new ArrayList<>();

        if (parentMatchId != null) {
            H2HMatch parentMatch = h2hObjectFactory.getInstance(stat, compSeasonKey, parentMatchId);

            participantIds.add(parentMatch.getParticipant1Id());
            participantIds.add(parentMatch.getParticipant2Id());
        }
        else
            participantIds.addAll(factory.getPhaseParticManager(stat)
                    .getParticipantIds(compSeasonPhase.getCompSeasonPhaseKey()));

        return participantIds;
    }

    private void writeParticipantSelect(List<? extends Participant> participants, String label,
                                        String name, Integer selOption, Writer w) throws IOException {
        w.append("<span>");
        w.append(label);
        w.append(": <select name=\"");
        w.append(name);
        w.append("\"");
        if (selOption != null)
            w.append(" disabled=\"true\"");
        w.append(">\n<option value=\"0\">-</option>\n");

        String intValSelOption = getIntValueForScriptTag(selOption);

        for (Participant participant : participants) {
            w.append("<option value=\"");
            w.append(Integer.toString(participant.getId()));
            w.append("\"");
            if (intValSelOption.equals(String.valueOf(participant.getId())))
                w.append(" selected");
            w.append(">");
            w.append(participant.getDescription());
            w.append("</option>\n");
        }

        w.append("</select></span><br/>\n");
    }
}
