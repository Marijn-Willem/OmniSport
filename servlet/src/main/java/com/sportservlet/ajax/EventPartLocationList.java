package com.sportservlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.EventPartLocation;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.manager.EventPartLocationManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventPartLocationList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEventPartKey csepKey = getCompSeasonEventPartKey(req);

        List<EventPartLocation> eventPartLocations = new EventPartLocationManager(stat).getEventPartLocations(csepKey);
        new DbCalculation(stat).setEventPartLocationStringFields(eventPartLocations);
        eventPartLocations.sort(new DescribedEntityDescription());

        for (EventPartLocation eventPartLocation : eventPartLocations)
            ServletUtil.writeOption(eventPartLocation.getEventPartLocationId(), getOutputString(eventPartLocation), resp.getWriter());
    }

    private String getOutputString(EventPartLocation eventPartLocation) {
        return Util.concatStringsWithDelimiter(eventPartLocation.getDescription(),
                eventPartLocation.getRoleName(), " - ");
    }
}
