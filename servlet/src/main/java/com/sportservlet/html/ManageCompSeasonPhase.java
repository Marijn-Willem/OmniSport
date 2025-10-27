package com.sportservlet.html;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.PhaseType;
import com.sports.entity.comparator.CompSeasonPhaseParentOrder;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.PhaseTypeManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class ManageCompSeasonPhase extends ManageEntity {
    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("compseasonphase");
    }

    @Override
    protected String getEntityIdName() {
        return "pid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        CompSeasonKey csk = new CompSeasonKey(competitionId, seasonId);
        CompSeasonPhaseManager cspm = new CompSeasonPhaseManager(stat);

        List<CompSeasonPhase> parentPhases;

        CompSeasonPhase csp = null;

        if ("u".equals(mode)) {
            int phaseId = Integer.parseInt(req.getParameter("pid"));

            csp = cspm.getCompSeasonPhase(new CompSeasonPhaseKey(csk, phaseId));
            parentPhases = !csp.isKnockoutParent() ? cspm.getKnockoutCompSeasonPhases(csk) :
                    new ArrayList<>();
        }
        else
            parentPhases = cspm.getKnockoutCompSeasonPhases(csk);

        new DbCalculation(stat).setPhaseDescriptionsFromTypes(parentPhases);
        parentPhases.sort(new CompSeasonPhaseParentOrder());

        LinkedHashMap<Integer, String> ppIdMap = new LinkedHashMap<>();
        ppIdMap.put(0, "-");

        for (CompSeasonPhase parentPhase : parentPhases)
            ppIdMap.put(parentPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId(), parentPhase.getDescription());

        List<PhaseType> phaseTypes = new PhaseTypeManager(stat).getNonParentPhaseTypeList();
        phaseTypes.sort(new NamedEntityName());

        LinkedHashMap<Integer, String> ptIdMap = getLinkedHashMapFromNamedIntEntities(phaseTypes, false);

        Writer w = res.getWriter();

        writeSelectWithLabel("Parent phase", "ppid", ppIdMap, csp != null ? csp.getParentPhaseId() : null, w);
        writeNumericTextField("Round", "rn", csp != null ? csp.getRound() : null, w);
        writeNumericTextField("Best of 1", "bo1", csp != null ? csp.getBestOf1() : null, w);
        writeNumericTextField("Best of 2", "bo2", csp != null ? csp.getBestOf2() : null, w);
        writeNumericTextField("Best of decider", "bod", csp != null ? csp.getBestOfDec() : null, w);
        writeCheckbox("Finished", "fn", csp != null && csp.isFinished(), w);
        writeDateTimeField("Start date", "sd", csp != null ? csp.getStartDate() : null, w);
        writeDateTimeField("End date", "ed", csp != null ? csp.getEndDate() : null, w);
        writeCheckbox("Standing", "st", csp != null && csp.isHasStanding(), w);
        writeCheckbox("Knockout parent", "kop", csp != null && csp.isKnockoutParent(), w);
        writeNumericTextField("Parent order", "po", csp != null ? csp.getParentOrder() : null, w);
        writeNumericTextField("Expand factor", "ef", csp != null ? csp.getExpandFactor() : null, w);
        writeCheckbox("Division standings", "ds", csp != null && csp.isHasDivisionStandings(), w);
        writeSelectWithLabel("Phase type", "ptid", ptIdMap, csp != null ? csp.getPhaseTypeId() : null, w);
        writeCheckbox("Parent matches", "pm", csp != null && csp.isHasParentMatches(), w);
    }
}
