package com.sportservlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.Gender;
import com.sports.entity.SportEvent;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.SportEventManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SportEventList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int spid = Integer.parseInt(req.getParameter("spid"));
        List<SportEvent> sportEvents = new SportEventManager(stat).getSportEventList(Collections.singletonList(spid));
        sportEvents.sort(new AliasableName());

        Map<SportEventKey, Boolean> hasFixedPartsMap = new DbCalculation(stat).getSportEventHasFixedPartsMap(
                sportEvents.stream().map(x -> new SportEventKey(spid, x.getSportEventId())).collect(Collectors.toList())
        );

        Writer w = resp.getWriter();

        for (SportEvent sportEvent : sportEvents) {
            SportEventKey sportEventKey = new SportEventKey(spid, sportEvent.getSportEventId());
            w.append("<option ");
            if (hasFixedPartsMap.get(sportEventKey))
                w.append("fp=\"true\" ");
            w.append("value=\"");
            w.append(Integer.toString(sportEvent.getSportEventId()));
            w.append("\">");
            w.append(getOptionContent(sportEvent));
            w.append("</option>\n");
        }
    }

    private String getOptionContent(SportEvent sportEvent) {
        return sportEvent.getName() + " - (" + Gender.getGenderNameFromId(sportEvent.getGenderId()) + ")";
    }
}
