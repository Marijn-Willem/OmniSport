package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class EventTeamImport extends SuperEntityImport {
    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("eventteam");
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("eid", getIntValuedParameterValue(req, "eid"), w);
    }

    @Override
    protected String getOnClick(HttpServletRequest req) {
        return "importEventTeams();";
    }
}
