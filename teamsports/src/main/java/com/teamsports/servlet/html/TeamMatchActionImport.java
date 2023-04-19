package com.teamsports.servlet.html;

import com.sports.entity.Sport;
import com.sports.entity.manager.CompetitionManager;
import com.sportservlet.html.EntityImportFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class TeamMatchActionImport extends EntityImportFile {
    private int spid;

    @Override
    protected String getOnClick(HttpServletRequest req) {
        return "processImport()";
    }

    @Override
    protected String getImportDir() {
        return "TeamMatchAction";
    }

    @Override
    protected boolean getNameFilter(String name) {
        String sportName = Sport.getNameFromId(spid);

        return sportName != null && name.startsWith(sportName);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        writeVarInScriptTag("spid", spid, w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        spid = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
        return "CompSeasonPortal?spid=" + spid + "&" + compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsSpecificList.add("teammatchaction");
        cssList.add("styling");
    }
}
