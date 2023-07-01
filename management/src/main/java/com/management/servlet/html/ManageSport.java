package com.management.servlet.html;

import com.sports.entity.Sport;
import com.sports.entity.manager.SportManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageSport extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "SportPortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sport");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "spid";
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        SportManager sm = new SportManager(stat);
        Sport sport = null;

        if ("u".equals(mode)) {
            int spId = Integer.parseInt(req.getParameter("spid"));
            sport = sm.getSport(spId);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", sport != null ? sport.getName() : null, w);
        writeCheckbox("Is Team", "it", sport != null && sport.isTeam(), w);
        writeCheckbox("Is H2H", "h2h", sport != null && sport.isH2H(), w);
        writeCheckbox("Has match parts", "hmp", sport != null && sport.isHasMatchParts(), w);
    }
}
