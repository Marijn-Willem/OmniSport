package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public abstract class EventPersonImport extends SuperEntityImport {
    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("eventperson");
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonEventVarsInScriptTag(req, w);
    }

    protected String getOnClick(HttpServletRequest req) {
        return "processEventPersonImportLoader.loadElement();";
    }
}
