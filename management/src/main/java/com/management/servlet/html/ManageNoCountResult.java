package com.management.servlet.html;

import com.sports.entity.NoCountResult;
import com.sports.entity.manager.NoCountResultManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageNoCountResult extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "NoCountResultPortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("nocountresult");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "ncrid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        NoCountResult noCountResult = null;

        if (!"i".equals(mode)) {
            int id = getIntValuedParameterValue(req, "ncrid");
            noCountResult = new NoCountResultManager(stat).getEntityFromId(id);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", noCountResult != null ? noCountResult.getName() : null, w);
    }
}
