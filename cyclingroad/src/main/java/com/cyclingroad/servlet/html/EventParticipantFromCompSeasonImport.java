package com.cyclingroad.servlet.html;

import com.sports.entity.Sport;
import com.sportservlet.html.SuperEntityImport;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EventParticipantFromCompSeasonImport extends SuperEntityImport {
    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("eid", getIntValuedParameterValue(req, "eid"), w);
    }

    @Override
    protected String getOnClick(HttpServletRequest req) {
        return "insertEventParticipantsFromCompSeason();";
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPortal?spid=" + Sport.sportIdCyclingRoad + "&" + compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("eventparticipant");
        cssList.add("styling");
    }
}
