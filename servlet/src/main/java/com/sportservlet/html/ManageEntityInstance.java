package com.sportservlet.html;

import com.sports.entity.EntityInstance;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.logic.factory.EntityInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageEntityInstance<S extends EntityInstanceKey, T extends EntityInstance> extends ManageEntity {
    abstract EntityInstanceFactory<S, T> getFactory();
    abstract String getServletNameReturnPath();
    abstract void writeSpecificFields(Statement stat, T entityInstance, Writer w) throws SQLException, IOException;

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

        return getServletNameReturnPath() + "?eid=" + eid + "&enm=" + enm;
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

        T entityInstance = getFactory().getEntity(stat, eid, eiid);

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", entityInstance.getName(), w);
        writeDateTimeField("Start date", "sd", entityInstance.getStartDate(), true, w);
        writeDateTimeField("End date", "ed", entityInstance.getEndDate(), true, w);
        writeSpecificFields(stat, entityInstance, w);
    }
}
