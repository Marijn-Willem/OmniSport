package com.cyclingroad.servlet.ajax;

import com.sports.calc.alcifo.Calculation;
import com.sports.calc.cyclingroad.Scraper;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.SportEventManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.flush.CyclingRoadStageFlusher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Scrape extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        int csepid = getIntValuedParameterValue(req, "csepid");
        CompSeasonEventKey cseKey = getCompSeasonEventKey(req);
        CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(cseKey, csepid);

        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);
        SportEvent sportEvent = new SportEventManager(stat).getEntityFromSuperKey(compSeasonEvent.getSportEventKey());

        AlcifoPartParticipantManager<? extends SuperKey,
                CompSeasonEventPartKey,
                ? extends AlcifoPartParticipant> partParticipantManager = Calculation.getAlcifoParticipantFactory(sportEvent)
                .getEventPartParticipantFactory().getManager(stat);

        List<? extends AlcifoPartParticipant> partParticipants = partParticipantManager.getPartParticipantList(csepKey);

        Writer w = resp.getWriter();

        if (!partParticipants.isEmpty()) {
            CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepKey);

            String url = Util.concatStringsWithDelimiter(compSeasonEvent.getExternalSource(),
                    compSeasonEventPart.getExternalSource(), "/");

            Scraper scraper = new Scraper(url, csepKey, stat);
            scraper.scrape();

            if (scraper.isSucceeded()) {
                w.append("<div>Scrape succeeded</div>\n");
                w.append("<div>The following persons have been newly created:</div>\n");
                for (String newPersonName : scraper.getNewPersonNames()) {
                    w.append("<div>");
                    w.append(newPersonName);
                    w.append("</div>\n");
                }

                cacheFlusher = new CyclingRoadStageFlusher(csepKey);
            }
            else
                w.append("Scrape failed");
        }
        else
            w.append("Already participants in this event");
    }
}
