package com.sportservlet.ajax;

import com.sports.entity.EventDisciplinePart;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.manager.EventDisciplinePartManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventDisciplinePartList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEventPartKey csepKey = getCompSeasonEventPartKey(stat, req);

        List<EventDisciplinePart> eventDisciplineParts = new EventDisciplinePartManager(stat).getEventDisciplineList(csepKey);
        eventDisciplineParts.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (EventDisciplinePart eventDisciplinePart : eventDisciplineParts)
            ServletUtil.writeOption(eventDisciplinePart.getEventDisciplinePartId(), eventDisciplinePart.getName(), w);
    }
}
