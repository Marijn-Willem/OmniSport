package com.management.servlet.html;

import com.sports.entity.PhaseType;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.PhaseTypeManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

public class ManagePhaseType extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "PhaseTypePortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("phasetype");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "ptid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        PhaseTypeManager ptm = new PhaseTypeManager(stat);
        PhaseType phaseType = null;

        if (!"i".equals(mode)) {
            int id = getIntValuedParameterValue(req, "ptid");
            phaseType = ptm.getEntityFromId(id);
        }

        Writer w = res.getWriter();

        List<PhaseType> parentPhaseTypes = phaseType != null ? ptm.getParentPhaseTypeListWithoutId(phaseType.getId()) :
                ptm.getParentPhaseTypeList();
        parentPhaseTypes.sort(new NamedEntityName());

        LinkedHashMap<Integer, String> parentMap = getLinkedHashMapFromNamedIntEntities(parentPhaseTypes, true);

        boolean isParent = phaseType != null && phaseType.isParent();

        writeTextFieldWithLabel("Name", "nm", phaseType != null ? phaseType.getName() : null, w);
        writeCheckbox("Is parent", "ip", isParent, false, "handleClickIsParent()", w);
        writeSelectWithLabel("Parent type", "pptid", parentMap,
                phaseType != null ? phaseType.getParentId() : null, isParent, w);
    }
}
