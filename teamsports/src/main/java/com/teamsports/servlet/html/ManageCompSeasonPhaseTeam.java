package com.teamsports.servlet.html;

import com.sports.entity.CompSeasonPhaseTeam;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.CompSeasonPhaseTeamManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageCompSeasonPhaseTeam extends ManageEntity {
    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPhaseTeamList?" + compSeasonUrlParameters + "&pid=" +
                getIntValuedParameterValue(req, "pid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonphaseteam");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return null;
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonPhaseVarsInScriptTag(req, w);
        writeVarInScriptTag("tid", getIntValuedParameterValue(req, "tid"), w);
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        if (!"i".equals(mode)) {
            int pid = getIntValuedParameterValue(req, "pid");
            int tid = getIntValuedParameterValue(req, "tid");

            CompSeasonPhaseTeamKey compSeasonPhaseTeamKey = new CompSeasonPhaseTeamKey(
                    new CompSeasonPhaseKey(compSeasonKey, pid), tid);

            CompSeasonPhaseTeam compSeasonPhaseTeam = new CompSeasonPhaseTeamManager(stat).getEntityFromSuperKey(compSeasonPhaseTeamKey);

            Writer w = res.getWriter();

            writeTextFieldWithLabel("Points correction", "pc",
                    String.valueOf(compSeasonPhaseTeam.getPointsCorrection()), w);
        }
    }

    @Override
    protected String getUpdateId(HttpServletRequest req) {
        return "";
    }
}
