package com.sportservlet.html;

import com.sports.entity.*;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ManageParticipantsCompSeasonPhase extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("phaseparticipants");
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonPhaseVarsInScriptTag(req, w);
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int phaseId = Integer.parseInt(req.getParameter("pid"));

        CompSeasonPhaseKey parentKey = new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId), phaseId);
        CompSeasonPhase csp = new com.sports.calc.h2hsports.DbCalculation(stat).getFirstRoundFromParent(parentKey);

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

        ParticipantManager<? extends Participant> pm = factory.getParticipantManager(stat);
        CompSeasonParticipantManager<? extends CompSeasonParticipantKey, ? extends SuperKeyEntity> cspm =
                factory.getCompSeasonParticipantManager(stat);
        CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey> csppm = factory.getPhaseParticManager(stat);

        Set<Integer> idsInPhase = new HashSet<>() {{
            if (csp != null)
                addAll(csppm.getParticipantIds(csp.getCompSeasonPhaseKey()));
        }};

        List<Integer> csIds = cspm.getParticipantIdsCompSeason(parentKey.getSuperKey());
        List<? extends Participant> participantList = pm.getParticipantList(csIds);
        participantList.sort(new DescribedEntityDescription());

        Writer w = res.getWriter();

        for (Participant participant : participantList)
            w.append(getCheckbox(participant, idsInPhase));

        w.append("<input type=\"button\" onclick=\"addPhaseParticipantsLoader.loadElement();\" ");
        w.append("value=\"Add Participants to Phase\" /><br/>\n");
        w.append("<div id=\"div_pp\"></div>\n");
    }

    private String getCheckbox(Participant participant, Set<Integer> idsInPhase) {
        boolean isChecked = idsInPhase.contains(participant.getId());

        return "<input type=\"checkbox\" name=\"ptid\" value=\"" + participant.getId() + "\"" +
                (isChecked ? " checked=\"checked\"" : "") + ">" + participant.getDescription() + "</input><br/>\n";
    }
}
