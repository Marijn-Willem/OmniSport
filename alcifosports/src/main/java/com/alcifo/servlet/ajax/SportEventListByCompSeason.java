package com.alcifo.servlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.Gender;
import com.sports.entity.SportEvent;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.SportEventManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class SportEventListByCompSeason extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<CompSeasonEvent> compSeasonEvents = new CompSeasonEventManager(stat).getCompSeasonEvents(compSeasonKey);
        List<SportEventKey> seKeys = compSeasonEvents.stream().map(CompSeasonEvent::getSportEventKey)
                .collect(Collectors.toList());

        List<SportEvent> sportEvents = new SportEventManager(stat).getSportEventListByKeys(seKeys);
        sportEvents.sort(new AliasableName());

        Writer w = resp.getWriter();

        for (SportEvent sportEvent : sportEvents)
            writeOption(sportEvent, w);
    }

    private void writeOption(SportEvent sportEvent, Writer w) throws IOException {
        w.append("<option value=\"");
        w.append(Integer.toString(sportEvent.getSportEventId()));
        w.append("\">");
        w.append(Util.concatStringsWithDelimiter(Util.convertNullStringToEmpty(sportEvent.getName()),
                Util.getStringBetweenBracketsOrEmptyString(Gender.getGenderNameFromId(sportEvent.getGenderId())),
                " - "));
        w.append("</option>\n");
    }
}
