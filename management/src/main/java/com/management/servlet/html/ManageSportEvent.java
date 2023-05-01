package com.management.servlet.html;

import com.sports.entity.SportEvent;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.SportEventManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public class ManageSportEvent extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int spid = Integer.parseInt(req.getParameter("spid"));
        return "SportEventPortal?spid=" + spid;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sportevent");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "eid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        w.append("const spid = ");
        w.append(req.getParameter("spid"));
        w.append(";\n");
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int spid = Integer.parseInt(req.getParameter("spid"));
        SportEventManager sm = new SportEventManager(stat);
        SportEvent sportEvent = null;

        if ("u".equals(mode)) {
            int eid = Integer.parseInt(req.getParameter("eid"));
            SportEventKey sek = new SportEventKey(spid, eid);
            sportEvent = sm.getSportEventListByKeys(Collections.singletonList(sek)).get(0);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", sportEvent != null ? sportEvent.getName() : null, w);
        writeCheckbox("Points sort ascending", "psa", sportEvent != null && sportEvent.isPointsSortAsc(), w);
        writeCheckbox("Is Team", "it", sportEvent != null && sportEvent.isTeam(), w);
    }
}
