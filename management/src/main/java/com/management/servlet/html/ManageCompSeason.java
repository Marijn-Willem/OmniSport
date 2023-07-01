package com.management.servlet.html;

import com.sports.entity.CompSeason;
import com.sports.entity.manager.CompSeasonManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageCompSeason extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonManagementPortal?" + compSeasonUrlParameters;
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseason");
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
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        CompSeason compSeason = new CompSeasonManager(stat).getCompSeason(compSeasonKey);

        Writer w = res.getWriter();

        writeNumericTextField("Tries bonus absolute", "tab", compSeason.getTriesAbsBonus(), w);
        writeNumericTextField("Tries bonus relative", "trb", compSeason.getTriesRelBonus(), w);
        writeNumericTextField("Loss difference bonus", "ldb", compSeason.getLossDiffBonus(), w);
        writeDateTimeField("Start date", "sd", compSeason.getStartDate(), w);
        writeDateTimeField("End date", "ed", compSeason.getEndDate(), w);
    }
}
