package com.sportservlet.html;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SportManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;
import com.sports.logic.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class ManageH2HMatch extends ManageEntity {
    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("h2hmatch");
    }

    protected void writeSpecificFields(H2HMatch h2HMatch, Writer w) throws IOException { }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"handleChangePhase();\">\n");
    }

    @Override
    protected String getEntityIdName() {
        return "mid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);
        Sport sport = new SportManager(stat).getSport(competition.getSportId());
        String participantString = sport.isTeam() ? "Team" : competition.isH2hDouble() ? "Double" : "Person";

        CompSeasonParticipantFactory<? extends CompSeasonParticipantKey, ? extends SuperKeyEntity> factory =
                new com.sports.logic.calculation.DbCalculation(stat).getCompSeasonParticipantFactory(competition);
        H2HObjectFactory<? extends H2HMatchKey, ? extends H2HMatch> h2hObjectFactory = factory.getH2HObjectFactory();

        H2HMatch h2HMatch = null;

        DbCalculation dbCalc = new DbCalculation(stat);

        if (!"i".equals(mode)) {
            int matchId = Integer.parseInt(req.getParameter("mid"));

            h2HMatch = dbCalc.retrieveH2HMatch(h2hObjectFactory, compSeasonKey, matchId);
        }

        List<CompSeasonPhase> compSeasonPhases = new CompSeasonPhaseManager(stat).getNonKnockoutCompSeasonPhases(compSeasonKey);
        dbCalc.setPhaseDescriptionsFromTypes(compSeasonPhases);
        compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());
        Map<Integer, List<CompSeasonPhase>> participantPhaseMap = dbCalc.getParticipantCompSeasonPhaseMap(compSeasonPhases);

        List<Integer> participantIds = factory.getCompSeasonParticipantManager(stat).getParticipantIdsCompSeason(compSeasonKey);
        List<? extends Participant> participants = factory.getParticipantManager(stat).getParticipantList(participantIds);
        participants.sort(new DescribedEntityDescription());

        LinkedHashMap<Integer, String> phaseOptions = new LinkedHashMap<>();

        for (CompSeasonPhase compSeasonPhase : compSeasonPhases)
            phaseOptions.put(compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId(), compSeasonPhase.getDescription());

        Writer w = res.getWriter();
        writeSelectWithLabel("Phase", "pid", phaseOptions, getCompSeasonPhaseAttributes(compSeasonPhases),
                h2HMatch != null ? h2HMatch.getCompSeasonPhaseId() : null, !"i".equals(mode),
                "handleChangePhase()", w);
        writeParticipantSelect(participants, participantPhaseMap, participantString + " 1", "p1id",
                h2HMatch != null ? h2HMatch.getParticipant1Id() : null, w);
        writeParticipantSelect(participants, participantPhaseMap, participantString + " 2", "p2id",
                h2HMatch != null ? h2HMatch.getParticipant2Id() : null, w);
        writeNumericTextField("Score " + participantString + " 1", "p1s", h2HMatch != null ? h2HMatch.getScore1_1() : null, w);
        writeNumericTextField("Score " + participantString + " 2", "p2s", h2HMatch != null ? h2HMatch.getScore1_2() : null, w);
        writeDateTimeField("Date", "dt", h2HMatch != null ?  h2HMatch.getDate() : null, w);
        writeCheckbox(participantString + " 1 Start", "p1st", h2HMatch != null && h2HMatch.isParticipant1Start(), w);
        writeCheckbox(participantString + " 1 Win", "p1w", h2HMatch != null && h2HMatch.isParticipant1Win(), true, w);
        writeCheckbox(participantString + " 2 Win", "p2w", h2HMatch != null && h2HMatch.isParticipant2Win(), true, w);
        writeSpecificFields(h2HMatch, w);
    }

    private Map<Integer, String> getCompSeasonPhaseAttributes(List<CompSeasonPhase> compSeasonPhases) {
        return new HashMap<>() {{
            compSeasonPhases.forEach(x -> {
                String dts = x.getStartDate() != null ? "dts=\"" + Util.convertDateTimeToString(x.getStartDate()) + "\"" : null;
                String dte = x.getEndDate() != null ? "dte=\"" + Util.convertDateTimeToString(x.getEndDate()) + "\"" : null;

                String attributes = Util.concatStringsWithDelimiter(dts, dte, " ");

                if (!Util.isEmptyString(attributes))
                    put(x.getCompSeasonPhaseKey().getCompSeasonPhaseId(), attributes);
            });
        }};
    }

    private void writeParticipantSelect(List<? extends Participant> participants,
                                        Map<Integer, List<CompSeasonPhase>> participantPhaseMap, String label,
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
            w.append("\" pid=\"");
            if (participantPhaseMap.containsKey(participant.getId()))
                w.append(getPidString(participantPhaseMap.get(participant.getId())));
            w.append("\"");
            if (intValSelOption.equals(String.valueOf(participant.getId())))
                w.append(" selected");
            w.append(">");
            w.append(participant.getDescription());
            w.append("</option>\n");
        }

        w.append("</select></span><br/>\n");
    }

    private String getPidString(List<CompSeasonPhase> compSeasonPhases) {
        List<String> pidList = new ArrayList<>();

        for (CompSeasonPhase compSeasonPhase : compSeasonPhases)
            pidList.add(String.valueOf(compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId()));

        return Util.concatStrings(pidList, ",");
    }
}
