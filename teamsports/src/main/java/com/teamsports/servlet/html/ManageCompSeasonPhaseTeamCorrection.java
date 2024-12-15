package com.teamsports.servlet.html;

import com.sports.entity.CompSeasonPhaseTeamCorrection;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamCorrectionKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.CompSeasonPhaseTeamCorrectionManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageCompSeasonPhaseTeamCorrection extends ManageEntity {
    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);

        writeCompSeasonPhaseVarsInScriptTag(req, w);
        writeVarInScriptTag("tid", getIntValuedParameterValue(req, "tid"), w);
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) { }

    @Override
    protected String getEntityIdName() {
        return "csptcid";
    }

    @Override
    protected String getBasicReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        int pid = getIntValuedParameterValue(req, "pid");
        int tid = getIntValuedParameterValue(req, "tid");

        CompSeasonPhaseTeamCorrection csptc = null;

        if (!"i".equals(mode)) {
            int csptcid = getIntValuedParameterValue(req, "csptcid");

            CompSeasonPhaseTeamCorrectionKey csptcKey = new CompSeasonPhaseTeamCorrectionKey(
                    new CompSeasonPhaseTeamKey(new CompSeasonPhaseKey(compSeasonKey, pid), tid), csptcid
            );

            csptc = new CompSeasonPhaseTeamCorrectionManager(stat).getEntityFromSuperKey(csptcKey);
        }

        Writer w = res.getWriter();

        writeDateTimeField("Date", "dt", csptc != null ? csptc.getDate() : null, w);
        writeNumericTextField("Points correction", "pc", csptc != null ? csptc.getPointsCorrection() : null, w);
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {

    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonphaseteamcorrection");
        cssList.add("styling");
    }
}
