package com.sportservlet.ajax;

import com.sports.entity.EntityInstance;
import com.sports.entity.comparator.EntityInstanceStartDate;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EntityInstanceList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int eid = getIntValuedParameterValue(req, "eid");
        String enm = req.getParameter("enm");

        EntityInstanceFactory<? extends EntityInstanceKey, ? extends EntityInstance> factory =
                Calculation.getEntityInstanceFactory(enm);
        assert factory != null;
        List<? extends EntityInstance> entityInstances = factory.getManager(stat).getInstancesForEntity(eid);

        entityInstances.sort(new EntityInstanceStartDate());

        Writer w = resp.getWriter();

        for (EntityInstance entityInstance : entityInstances) {
            String line = Util.convertEmptyDateTimeToDateString(entityInstance.getStartDate(), "No start date") +
                    " - " +
                    Util.convertEmptyDateTimeToDateString(entityInstance.getEndDate(), "No end date");

            ServletUtil.writeOption(entityInstance.getEntityInstanceId(), line, w);
        }
    }
}
