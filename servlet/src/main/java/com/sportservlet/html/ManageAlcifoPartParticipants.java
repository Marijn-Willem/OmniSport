package com.sportservlet.html;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.SportDisciplineManager;
import com.sports.logic.util.Util;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class ManageAlcifoPartParticipants<P extends Participant, PK extends SuperKey> extends SuperHtmlServlet implements AbstractHtmlServlet {
    private final AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends SuperKeyEntity,
            P,
            ? extends AlcifoParticipantKey,
            ? extends AlcifoParticipant,
            ? extends SuperKey,
            PK,
            ? extends AlcifoPartParticipant,
            ? extends SuperKey,
            ? extends AlcifoPartParticipant> factory = getFactory();
    private PK partKey;
    private SportDisciplineKey sportDisciplineKey;

    abstract PK getPartKey(HttpServletRequest req);
    abstract AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends SuperKeyEntity,
            P,
            ? extends AlcifoParticipantKey,
            ? extends AlcifoParticipant,
            ? extends SuperKey,
            PK,
            ? extends AlcifoPartParticipant,
            ? extends SuperKey,
            ? extends AlcifoPartParticipant> getFactory();
    abstract void initSpecificJsProperties();

    void writeSpecificScriptTagVars(HttpServletRequest req, Writer w) throws IOException { }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("ranktable");
        initSpecificJsProperties();
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        partKey = getPartKey(req);

        CompSeasonEventPartKey csepKey = getCompSeasonEventPartKey(req);
        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getEntityFromSuperKey(csepKey);
        sportDisciplineKey = compSeasonEventPart.getSportDisciplineKey();
        int rtid = new SportDisciplineManager(stat).getEntityFromSuperKey(sportDisciplineKey).getResultTypeId();

        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("cseid", getIntValuedParameterValue(req, "cseid"), w);
        writeVarInScriptTag("csepid", getIntValuedParameterValue(req, "csepid"), w);
        writeSpecificScriptTagVars(req, w);
        writeVarInScriptTag("rtid", rtid, w);
        ServletUtil.writeNoCountResList(getNoCountResultMap(stat), w);
        w.append("let md = 'a';\n");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        List<P> participants = new DbCalculation(stat).getFullRankingInPart(factory, partKey);

        SportDiscipline sd = new SportDisciplineManager(stat).getEntityFromSuperKey(sportDisciplineKey);

        Writer w = res.getWriter();

        w.append("<table id=\"tblPt\">\n");
        w.append("<tr><th>Rank</th><th>Name</th><th>Points</th><th>NoCountResult</th></tr>\n");
        for (Participant participant : participants)
            writeTableRow(participant, sd.getResultTypeId(), stat, w);
        w.append("</table>\n");

        w.append("<table class=\"button_container\">\n");
        w.append("<tr><td><input type=\"button\" onclick=\"sortRank();\" value=\"Sort by rank\" /></td>");
        w.append("<td><input type=\"button\" onclick=\"sortPoints();\" value=\"Sort by points\" /></td><td/></tr>\n");
        w.append("<tr><td><input type=\"button\" onclick=\"setRanks();\" value=\"Set ranks\" /></td>");
        w.append("<td><input type=\"button\" onclick=\"clearRanks();\" value=\"Clear ranks\" /></td><td/></tr>\n");
        w.append("<tr><td><input type=\"button\" onclick=\"fillPoints();\" value=\"Fill points\" /></td>");
        w.append("<td><input type=\"button\" onclick=\"fillNoCountResults();\" value=\"Fill NoCount Results\" /></td><td/><tr/>\n");
        w.append("<tr>");
        if (sd.getResultTypeId() == ResultType.resultTypeIdTime)
            w.append("<td><input id=\"btnMd\" type=\"button\" onclick=\"switchMode();\" value=\"Set relative\" /></td>");
        else
            w.append("<td><input id=\"btnMd\" type=\"hidden\" /></td>");
        w.append("<td><input id=\"btnArr\" type=\"button\" onclick=\"switchArrange();\" value=\"Arrange participants\" /></td>");
        w.append("<td><input id=\"btnSw\" type=\"button\" onclick=\"switchRowSwitchMode();\" value=\"Switchable rows\" /></td></tr>\n");
        w.append("<tr><td><input type=\"button\" onclick=\"save();\" value=\"Save\" /></td><td/><td/></tr>\n");
        w.append("</table>\n");
        w.append("<div id=\"divPt\"></div>\n");
    }

    private void writeTableRow(Participant participant, int resultType, Statement stat, Writer w)
            throws SQLException, IOException {
        String pointsString = participant.getPoints() != null ? formatPoints(participant.getPoints(), resultType) : "";

        w.append("<tr ptid=\"");
        w.append(Integer.toString(participant.getId()));
        w.append("\"><td><input type=\"text\" value=\"");
        w.append(Util.convertIntegerToString(participant.getRank()));
        w.append("\" /></td><td>");
        w.append(participant.getDescription());
        w.append("</td><td><input type=\"text\" value=\"");
        w.append(pointsString);
        w.append("\" /></td><td><input type=\"text\" value=\"");
        w.append(Util.convertNullStringToEmpty(getNoCountResultMap(stat).get(participant.getNoCountResultId())));
        w.append("\" /></td></tr>\n");
    }

    private String formatPoints(int points, int resultType) {
        if (resultType == ResultType.resultTypeIdTime)
            return Util.getHMSStringFromMillis(points);

        return Integer.toString(points);
    }
}
