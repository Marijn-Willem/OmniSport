package com.management.servlet.html;

import com.sports.entity.EventPartName;
import com.sports.entity.key.EventPartNameKey;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.EventPartNameManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageEventPartName extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");

        return "EventPartNamePortal?spid=" + spid + "&eid=" + eid;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("eventpartname");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "epnid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeVarInScriptTag("spid", getIntValuedParameterValue(req, "spid"), w);
        writeVarInScriptTag("eid", getIntValuedParameterValue(req, "eid"), w);
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        EventPartNameManager epnm = new EventPartNameManager(stat);

        EventPartName eventPartName = null;

        if (!mode.equals("i")) {
            int spid = getIntValuedParameterValue(req, "spid");
            int eid = getIntValuedParameterValue(req, "eid");
            int epnid = getIntValuedParameterValue(req, "epnid");

            eventPartName = epnm.getEntityFromSuperKey(new EventPartNameKey(new SportEventKey(spid, eid), epnid));
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", eventPartName != null ? eventPartName.getName() : null, w);
    }
}
