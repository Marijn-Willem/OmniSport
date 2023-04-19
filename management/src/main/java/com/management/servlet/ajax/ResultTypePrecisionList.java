package com.management.servlet.ajax;

import com.sports.entity.ResultTypePrecision;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.ResultTypePrecisionManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ResultTypePrecisionList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        int rtid = getIntValuedParameterValue(req, "rtid");

        List<ResultTypePrecision> precisionList = new ResultTypePrecisionManager(stat).getPrecisionsForResultType(rtid);
        precisionList.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        ServletUtil.writeOption(0, "-", w);

        for (ResultTypePrecision precision : precisionList)
            ServletUtil.writeOption(precision.getResultTypePrecisionId(), precision.getName(), w);
    }
}
