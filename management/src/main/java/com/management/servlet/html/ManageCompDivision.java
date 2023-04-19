package com.management.servlet.html;

import com.sports.entity.CompDivision;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.manager.CompDivisionManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

public class ManageCompDivision extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompDivisionPortal?cid=" + competitionId;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compdivision");
        cssList.add("styling");
    }
    @Override

    protected void initAbstractProperties(HttpServletRequest req) {
        
    }

    @Override
    protected String getEntityIdName() {
        return "did";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeVarInScriptTag("cid", competitionId, w);
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        CompDivisionManager cdm = new CompDivisionManager(stat);
        CompDivision compDivision = null;

        if (!"i".equals(mode)) {
            int did = getIntValuedParameterValue(req, "did");
            CompDivisionKey cdk = new CompDivisionKey(competitionId, did);
            compDivision = cdm.getCompDivision(cdk);
        }

        List<CompDivision> compDivisions = cdm.getCompDivisions(competitionId);
        compDivisions.sort(new NamedEntityName());

        LinkedHashMap<Integer, String> parentDivisionMap = new LinkedHashMap<>();
        parentDivisionMap.put(0, "-");
        for (CompDivision cd : compDivisions)
            parentDivisionMap.put(cd.getCompDivisionId(), cd.getName());

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", compDivision != null ? compDivision.getName() : null, w);
        writeSelectWithLabel("Parent division", "pdid", parentDivisionMap, compDivision != null ? compDivision.getParentDivisionId() : null, w);
    }
}
