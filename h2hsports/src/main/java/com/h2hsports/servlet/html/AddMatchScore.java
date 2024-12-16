package com.h2hsports.servlet.html;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.H2HObjectFactory;

import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AddMatchScore extends SuperHtmlServlet {
    private CompSeasonPhaseKey compSeasonPhaseKey;
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

    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("entity");
        jsSpecificList.add("addmatch");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w)
            throws SQLException, IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("mid", getIntValuedParameterValue(req, "mid"), w);
        ServletUtil.writeNoCountResList(getNoCountResultMap(stat), w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "MatchOverview?" + compSeasonUrlParameters + "&pid=" + req.getParameter("pid");
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int h2hMatchId = Integer.parseInt(req.getParameter("mid"));

        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

        factory = new com.sports.logic.calculation.DbCalculation(stat).getCompSeasonParticipantFactory(competition);
        h2hObjectFactory = factory.getH2HObjectFactory();
        H2HMatch h2hMatch = new DbCalculation(stat).retrieveH2HMatch(h2hObjectFactory, compSeasonKey, h2hMatchId);

        compSeasonPhaseKey = new CompSeasonPhaseKey(compSeasonKey, h2hMatch.getCompSeasonPhaseId());

        Map<Integer, ? extends Participant> participantMap = getParticipantMap(stat);
        String personSelect = getPersonSelect(participantMap);

        Writer w = res.getWriter();

        w.append("<input name=\"cb_match\" type=\"checkbox\" onclick=\"toggleMatchRow();\" checked />");
        w.append("Add match score<br/>\n");
        w.append("<input name=\"cb_sets\" type=\"checkbox\" onclick=\"toggleSetRows();\" />Add sets<br/>\n");
        w.append("<table id=\"tbl_scr\" border=\"1\">\n");

        String line = "<tr><th>" + getParticipantElement(participantMap, personSelect,
                h2hMatch.getParticipant1Id(), true) +
                "</th><th/><th>" +
                getParticipantElement(participantMap, personSelect,
                        h2hMatch.getParticipant2Id(), false) +
                "</th></tr>\n";

        w.append(line);

        w.append(getInputRow("scr_m", "Match", false));

        CompSeasonPhase csp = new CompSeasonPhaseManager(stat).getCompSeasonPhase(compSeasonPhaseKey);

        for (int i = 0; i < csp.getBestOf1(); i++)
            w.append(getInputRow("scr_s" + (i + 1), "Set " + (i + 1), true));

        w.append(getInputRow("ncr", "NCR", true));

        w.append("</table>\n");
        w.append("<input type=\"button\" onclick=\"handleSubmit();\" value=\"Submit\" />\n");
        writeHiddenFieldsCompSeason(w);
        if (h2hMatch.getParticipant1Id() != null)
            writeHiddenField(w, getParticipantName(true), h2hMatch.getParticipant1Id());
        if (h2hMatch.getParticipant2Id() != null)
            writeHiddenField(w, getParticipantName(false), h2hMatch.getParticipant2Id());
        w.append("<br/>");
    }

    private String getInputRow(String name, String title, boolean hide) {
        return "<tr" + (hide ? " style=\"display:none\"" : "") + "><td>" +
                "<input name=\"" + name + "_1\" type=\"text\" /></td><td>" + title +
                "</td><td><input name=\"" + name + "_2\" type=\"text\" /></td></tr>\n";
    }

    private String getPersonSelect(Map<Integer, ? extends Participant> particMap) {
        StringBuilder select = new StringBuilder();

        for (Map.Entry<Integer, ? extends Participant> me : particMap.entrySet()) {
            select.append("<option value=\"");
            select.append(me.getKey());
            select.append("\">");
            select.append(me.getValue().getDescription());
            select.append("</option>\n");
        }

        return select.toString();
    }

    private Map<Integer, ? extends Participant> getParticipantMap(Statement stat) throws SQLException {
        CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey> csppm = factory.getPhaseParticManager(stat);
        ParticipantManager<? extends Participant> pm = factory.getParticipantManager(stat);

        List<Integer> particIds = csppm.getParticipantIds(compSeasonPhaseKey);
        Map<Integer, ? extends Participant> particMap = pm.getParticipantMap(particIds);

        List<? extends H2HMatch> matchListPhase = h2hObjectFactory.getManager(stat).getH2HMatchesFromCompSeasonPhases(
                Collections.singletonList(compSeasonPhaseKey)
        );

        for (H2HMatch match : matchListPhase)
            if (match.isFinished()) {
                particMap.remove(match.getParticipant1Id());
                particMap.remove(match.getParticipant2Id());
            }

        return particMap;
    }

    private String getParticipantElement(Map<Integer, ? extends Participant> particMap, String personSelect,
                                         Integer particId, boolean isPartic1) {
        if (particId != null)
            return particMap.get(particId).getDescription();

        return "<select name=\"" + getParticipantName(isPartic1) + "\">\n" + personSelect + "</select>\n";
    }

    private String getParticipantName(boolean isPartic1) {
        return "p" + (isPartic1 ? "1" : "2") + "id";
    }
}
