package com.teamsports.servlet.ajax;

import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompetitionManager;
import com.sportservlet.SuperResponseServlet;
import com.teamsports.servlet.util.ServletUtil;
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
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int phaseId = Integer.parseInt(req.getParameter("pid"));
        String divId = req.getParameter("did");

        CompSeasonKey csk = new CompSeasonKey(competitionId, seasonId);
        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(csk, phaseId);
        CompDivisionKey csd = divId != null ? new CompDivisionKey(competitionId, Integer.parseInt(divId)) : null;

        DbCalculation dbCalculation = new DbCalculation(stat);

        List<Team> standing = csd != null ? dbCalculation.getDivisionStanding(cspk, csd) :
                dbCalculation.getStandingCompSeasonPhase(cspk);

        Competition comp = new CompetitionManager(stat).getCompetition(competitionId);
        boolean bonusPoints = comp.getSportId() == Sport.sportIdRugby;
        boolean isDomesticUSA = comp.isDomestic() && comp.getGeoId() == Geo.geoIdUSA;

        Writer w = resp.getWriter();

        if (isDomesticUSA)
            ServletUtil.writeStandingUSAHeader(w);
        else
            ServletUtil.writeStandingHeader(w, bonusPoints);

        if (isDomesticUSA)
            ServletUtil.writeStandingUSA(w, standing);
        else
            ServletUtil.writeStanding(w, standing, bonusPoints, true);
    }
}
