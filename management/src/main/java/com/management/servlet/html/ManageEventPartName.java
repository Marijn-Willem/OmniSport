package com.management.servlet.html;

import com.sports.entity.EventPartName;
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
        return "EventPartNamePortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

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
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        EventPartNameManager epnm = new EventPartNameManager(stat);

        EventPartName eventPartName = null;

        if (!mode.equals("i")) {
            int epnid = getIntValuedParameterValue(req, "epnid");

            eventPartName = epnm.getEntityFromId(epnid);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", eventPartName != null ? eventPartName.getName() : null, w);
    }
}
