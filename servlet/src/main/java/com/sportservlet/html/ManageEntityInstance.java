package com.sportservlet.html;

import com.sports.entity.EntityInstance;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageEntityInstance extends ManageEntity {
    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("entityinstance");
    }

    @Override
    protected String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");
        String enm = req.getParameter("enm");

        String servletName = "Person".equals(enm) ? "EntityInstancePortalPerson" : "EntityInstancePortal";

        return servletName + "?eid=" + eid + "&enm=" + enm;
    }

    @Override
    protected String getEntityIdName() {
        return "eiid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeVarInScriptTag("eid", getIntValuedParameterValue(req, "eid"), w);
        w.append("enm = '");
        w.append(req.getParameter("enm"));
        w.append("';\n");
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int eid = getIntValuedParameterValue(req, "eid");
        int eiid = getIntValuedParameterValue(req, "eiid");

        EntityInstanceFactory<? extends EntityInstanceKey, ? extends EntityInstance> factory =
                Calculation.getEntityInstanceFactory(req.getParameter("enm"));
        assert factory != null;

        EntityInstance entityInstance = factory.getEntity(stat, eid, eiid);

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", entityInstance.getName(), w);
        writeDateTimeField("Start date", "sd", entityInstance.getStartDate(), true, w);
        writeDateTimeField("End date", "ed", entityInstance.getEndDate(), true, w);
    }
}
