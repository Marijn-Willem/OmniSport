package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddParticipantsToCompSeasonPhase extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int phaseId = Integer.parseInt(req.getParameter("pid"));
        String[] partIdsStr = req.getParameterValues("ptid");

        List<Integer> partIds = new ArrayList<>();

        if (partIdsStr != null)
            for (String str : partIdsStr)
                partIds.add(Integer.parseInt(str));

        CompSeasonPhaseKey parentKey = new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId), phaseId);
        CompSeasonPhase firstRound = new com.sports.calc.h2hsports.DbCalculation(stat).getFirstRoundFromParent(parentKey);

        String output;

        if (firstRound != null) {
            CompSeasonPhaseKey cspk = firstRound.getCompSeasonPhaseKey();
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
                    ? extends H2HMatchPartStat> factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);

            CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey,
                    ? extends CompSeasonPhaseParticipantKey,
                    ? extends SuperKeyEntity> csppm = factory.getPhaseParticManager(stat);
            List<Integer> curPartIds = csppm.getParticipantIds(cspk);

            List<Integer> partIdsToDel = Util.getElementsLeftNotInRight(curPartIds, partIds);
            List<Integer> partIdsToAdd = Util.getElementsLeftNotInRight(partIds, curPartIds);

            factory.deletePhaseParticipants(stat, cspk, partIdsToDel);
            factory.insertPhaseParticipantKeyList(stat, cspk, partIdsToAdd);

            output = "Participants successfully processed";
        }
        else
            output = "No phases present under this parent phase";

        resp.getWriter().append(output);
    }
}
