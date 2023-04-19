package com.sportservlet.ajax;

import com.sports.entity.EntityInstance;
import com.sports.entity.manager.EntityInstanceManager;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class ProcessInsertEntityInstance extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int eid = getIntValuedParameterValue(req, "eid");
        String enm = req.getParameter("enm");
        LocalDateTime dt = Util.convertStringToDateTime(req.getParameter("dt"));

        EntityInstanceFactory factory = Calculation.getEntityInstanceFactory(enm);
        assert factory != null;
        EntityInstanceManager manager = factory.getManager(stat);

        EntityInstance currentInstance = manager.getCurrentInstance(eid);
        currentInstance.setEndDate(dt.minusSeconds(1L));

        manager.update(factory.getKey(eid, currentInstance.getEntityInstanceId()), currentInstance);

        EntityInstance newInstance = factory.getInstance();
        newInstance.setStartDate(dt);
        newInstance.setName(currentInstance.getName());

        manager.insert(factory.getKey(eid, currentInstance.getEntityInstanceId() + 1), newInstance);

        resp.getWriter().append("New instance successfully inserted");
    }
}
