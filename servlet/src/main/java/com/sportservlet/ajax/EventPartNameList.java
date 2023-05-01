package com.sportservlet.ajax;

import com.sports.entity.EventPartName;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.EventPartNameManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventPartNameList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<EventPartName> eventPartNames = new EventPartNameManager(stat).getEventPartNames();
        eventPartNames.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (EventPartName eventPartName : eventPartNames)
            ServletUtil.writeOption(eventPartName.getId(), eventPartName.getName(), w);
    }
}
