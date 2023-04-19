package com.sportservlet.ajax;

import com.sports.entity.DisciplinePart;
import com.sports.entity.comparator.OrderableOrder;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.DisciplinePartManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DisciplinePartList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int spid = getIntValuedParameterValue(req, "spid");
        int did = getIntValuedParameterValue(req, "did");

        SportDisciplineKey sdk = new SportDisciplineKey(spid, did);

        List<DisciplinePart> disciplineParts = new DisciplinePartManager(stat).getDisciplinePartList(sdk);
        disciplineParts.sort(new OrderableOrder());

        for (DisciplinePart disciplinePart : disciplineParts)
            ServletUtil.writeOption(disciplinePart.getDisciplinePartId(), disciplinePart.getName(), resp.getWriter());
    }
}
