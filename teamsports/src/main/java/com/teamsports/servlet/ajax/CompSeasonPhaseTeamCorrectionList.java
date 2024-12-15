package com.teamsports.servlet.ajax;

import com.sports.entity.CompSeasonPhaseTeamCorrection;
import com.sports.entity.comparator.CompSeasonPhaseTeamCorrectionDate;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.CompSeasonPhaseTeamCorrectionManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonPhaseTeamCorrectionList extends SuperServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        int pid = getIntValuedParameterValue(req, "pid");
        int tid = getIntValuedParameterValue(req, "tid");

        CompSeasonPhaseTeamKey csptKey = new CompSeasonPhaseTeamKey(new CompSeasonPhaseKey(compSeasonKey, pid), tid);

        List<CompSeasonPhaseTeamCorrection> correctionList = new CompSeasonPhaseTeamCorrectionManager(stat)
                .getCorrectionsForCompSeasonPhaseTeam(csptKey);

        correctionList.sort(new CompSeasonPhaseTeamCorrectionDate());

        Writer w = resp.getWriter();

        for (CompSeasonPhaseTeamCorrection correction : correctionList)
            ServletUtil.writeOption(correction.getCompSeasonPhaseTeamCorrectionId(),
                    Util.convertDateTimeToDateString(correction.getDate()), w);
    }
}
