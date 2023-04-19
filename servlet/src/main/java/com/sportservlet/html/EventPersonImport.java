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
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int eventId = Integer.parseInt(req.getParameter("eid"));

        String line = "const cid = " + competitionId + ";\nconst sid = " + seasonId +
                ";\nconst eid = " + eventId + ";\n";

        w.append(line);
    }

    protected String getOnClick(HttpServletRequest req) {
        return "processEventPersonImportLoader.loadElement();";
    }
}
