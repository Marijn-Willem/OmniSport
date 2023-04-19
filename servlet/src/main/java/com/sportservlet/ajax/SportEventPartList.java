package com.sportservlet.ajax;

import com.sports.entity.SportEventPart;
import com.sports.entity.comparator.OrderableOrder;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.SportEventPartManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportEventPartList extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int spid = Integer.parseInt(req.getParameter("spid"));
        int eid = Integer.parseInt(req.getParameter("eid"));

        SportEventKey sek = new SportEventKey(spid, eid);

        List<SportEventPart> sportEventPartList = new SportEventPartManager(stat).getSportEventParts(sek, null);

        sportEventPartList.sort(new OrderableOrder());

        Writer w = resp.getWriter();

        for (SportEventPart sep : sportEventPartList)
            ServletUtil.writeOption(sep.getSportEventPartId(), sep.getName(), w);
    }
}
