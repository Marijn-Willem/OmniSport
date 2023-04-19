package com.management.servlet.html;

import com.sports.entity.DisciplinePart;
import com.sports.entity.key.DisciplinePartKey;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.DisciplinePartManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageDisciplinePart extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int spid = getIntValuedParameterValue(req, "spid");
        int did = getIntValuedParameterValue(req, "did");
        return "DisciplinePartPortal?spid=" + spid + "&did=" + did;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("disciplinepart");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "dpid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeVarInScriptTag("spid", getIntValuedParameterValue(req, "spid"), w);
        writeVarInScriptTag("did", getIntValuedParameterValue(req, "did"), w);
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int spid = getIntValuedParameterValue(req, "spid");
        int did = getIntValuedParameterValue(req, "did");

        SportDisciplineKey sdk = new SportDisciplineKey(spid, did);
        DisciplinePart dp =  null;

        if ("u".equals(mode)) {
            int dpid = getIntValuedParameterValue(req, "dpid");
            dp = new DisciplinePartManager(stat).getDisciplinePart(new DisciplinePartKey(sdk, dpid));
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", dp != null ? dp.getName() : null, w);
        writeNumericTextField("Order", "o", dp != null ? dp.getOrder() : null, w);
    }
}
