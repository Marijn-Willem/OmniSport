package com.cyclingroad.servlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.SportEvent;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonEventPartPorts extends com.sportservlet.ajax.CompSeasonEventPartPorts {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(getCompSeasonEventKey(req));
        int sportEventId = cse.getSportEventKey().getSportEventId();

        boolean showSetGeneralPoints = sportEventId == SportEvent.sportEventIdCyclingRoadGeneral;

        Writer w = resp.getWriter();

        writeManageParticipantButton(w);
        ServletUtil.writeGenericGoToButton("EventDisciplinePartPortal", parameters, "Manage discipline parts", w);
        writeInsertParticipantButton(w);
        if (showSetGeneralPoints) {
            w.append("<input type=\"button\" onclick=\"setGeneralClassificationPoints();\" ");
            w.append("value=\"Calculate general classification times\" /><br/>\n");
        }
        w.append("<input type=\"button\" onclick=\"scrape();\" value=\"Scrape\" /><br/>\n");
        w.append("<div id=\"divIns\"></div>\n");
    }
}
