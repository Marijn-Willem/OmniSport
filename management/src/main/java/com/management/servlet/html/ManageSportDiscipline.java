package com.management.servlet.html;

import com.sports.entity.ResultType;
import com.sports.entity.ResultTypePrecision;
import com.sports.entity.SportDiscipline;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.ResultTypePrecisionManager;
import com.sports.entity.manager.SportDisciplineManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageSportDiscipline extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "SportDisciplinePortal?spid=" + getIntValuedParameterValue(req, "spid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sportdiscipline");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "did";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeVarInScriptTag("spid", getIntValuedParameterValue(req, "spid"), w);
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int spid = getIntValuedParameterValue(req, "spid");

        SportDiscipline sd = null;

        if ("u".equals(mode)) {
            SportDisciplineManager sdm = new SportDisciplineManager(stat);
            int did = getIntValuedParameterValue(req, "did");
            sd = sdm.getSportDisciplineList(Collections.singletonList(new SportDisciplineKey(spid, did))).get(0);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", sd != null ? sd.getName() : null, w);
        writeSelectWithLabel("Result type", "rtid", ResultType.resultTypeLinkedHashMap,
                sd != null ? sd.getResultTypeId() : null, false, "handleSelectResultType()", w);
        writeResultTypePrecisionSelect(sd, stat, w);
    }

    private void writeResultTypePrecisionSelect(SportDiscipline sportDiscipline, Statement stat, Writer w) throws SQLException, IOException {
        List<ResultTypePrecision> precisions = new ArrayList<>() {{
            if (sportDiscipline != null)
                addAll(new ResultTypePrecisionManager(stat).getPrecisionsForResultType(sportDiscipline.getResultTypeId()));
        }};

        precisions.sort(new NamedEntityName());

        Integer rtpId = sportDiscipline != null ? sportDiscipline.getResultTypePrecisionId() : null;

        w.append("<span>Result type precision: <select id=\"rtpid\" name=\"rtpid\">\n");
        w.append("<option value=\"");
        w.append(getIntValueForScriptTag(null));
        w.append("\"");
        if (rtpId == null)
            w.append(" selected");
        w.append(">-</option>\n");
        for (ResultTypePrecision precision : precisions) {
            w.append("<option value=\"");
            w.append(getIntValueForScriptTag(precision.getResultTypePrecisionId()));
            w.append("\"");
            if (Integer.valueOf(precision.getResultTypePrecisionId()).equals(rtpId))
                w.append(" selected");
            w.append(">");
            w.append(precision.getName());
            w.append("</option>\n");
        }

        w.append("</select></span><br/>\n");
    }
}
