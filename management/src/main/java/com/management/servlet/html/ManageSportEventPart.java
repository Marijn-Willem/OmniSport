package com.management.servlet.html;

import com.sports.entity.SportDiscipline;
import com.sports.entity.SportEventPart;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.key.SportEventPartKey;
import com.sports.entity.manager.SportDisciplineManager;
import com.sports.entity.manager.SportEventPartManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

public class ManageSportEventPart extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");

        return "SportEventPartPortal?spid=" + spid + "&eid=" + eid;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sporteventpart");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "epid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");
        w.append("const spid = ");
        w.append(Integer.toString(spid));
        w.append(";\n");
        w.append("const eid = ");
        w.append(Integer.toString(eid));
        w.append(";\n");
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int spid = getIntValuedParameterValue(req, "spid");
        int eid = getIntValuedParameterValue(req, "eid");
        SportEventKey sek = new SportEventKey(spid, eid);

        SportEventPart sep = null;

        if ("u".equals(mode)) {
            int epid = getIntValuedParameterValue(req, "epid");
            SportEventPartKey sepk = new SportEventPartKey(sek, epid);

            sep = new SportEventPartManager(stat).getSportEventPart(sepk);
        }

        Writer w = res.getWriter();

        List<SportDiscipline> sportDisciplines = new SportDisciplineManager(stat).getSportDisciplinesForSport(spid);
        sportDisciplines.sort(new AliasableName());

        LinkedHashMap<Integer, String> sportDisciplineMap = new LinkedHashMap<>();
        for (SportDiscipline sportDiscipline : sportDisciplines)
            sportDisciplineMap.put(sportDiscipline.getSportDisciplineId(), sportDiscipline.getName());

        writeSelectWithLabel("Sport discipline", "did", sportDisciplineMap, sep != null ? sep.getSportDisciplineId() : null, w);
        writeTextFieldWithLabel("Name", "nm", sep != null ? sep.getName() : null, w);
        writeNumericTextField("Order", "o", sep != null ? sep.getOrder() : null, w);
        writeNumericTextField("Weight", "w", sep != null ? sep.getWeight() : null, w);
    }
}
