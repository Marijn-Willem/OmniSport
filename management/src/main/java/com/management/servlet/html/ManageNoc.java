package com.management.servlet.html;

import com.sports.entity.Noc;
import com.sports.entity.manager.NocManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageNoc extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "NocPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("noc");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "nid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Noc noc = null;

        if (!"i".equals(mode)) {
            int nid = getIntValuedParameterValue(req, "nid");
            noc = new NocManager(stat).getEntityFromId(nid);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", noc != null ? noc.getName() : null, w);
        writeSelectWithLabel("Geo", "geid", getGeoMapWithCountries(stat), noc != null ? noc.getGeoId() : null, w);
        w.append("<input type=\"button\" onclick=\"setNocNameFromGeoName();\" value=\"Set name\" /><br/>\n");
    }
}
