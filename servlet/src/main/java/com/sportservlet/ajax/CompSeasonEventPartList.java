package com.sportservlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.comparator.OrderableOrder;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class CompSeasonEventPartList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        CompSeasonEventKey cseKey = getCompSeasonEventKey(stat, req);
        List<CompSeasonEventPart> compSeasonEventParts = new CompSeasonEventPartManager(stat)
                .getCompSeasonEventPartsFromEvents(Collections.singletonList(cseKey));
        new DbCalculation(stat).setCompSeasonEventPartDescriptions(cseKey, compSeasonEventParts);

        compSeasonEventParts.sort(new OrderableOrder());

        Writer w = resp.getWriter();

        for (CompSeasonEventPart compSeasonEventPart : compSeasonEventParts)
            ServletUtil.writeOption(compSeasonEventPart.getCompSeasonEventPartId(),
                    compSeasonEventPart.getDescription(), w);
    }
}
