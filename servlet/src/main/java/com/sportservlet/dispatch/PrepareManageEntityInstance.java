package com.sportservlet.dispatch;

import com.sports.entity.EntityInstance;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class PrepareManageEntityInstance extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        String enm = req.getParameter("enm");
        EntityInstanceFactory<? extends EntityInstanceKey, ? extends EntityInstance> factory =
                Calculation.getEntityInstanceFactory(enm);

        dispatchURL = factory.getManagePath();
    }
}
