package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class MatchesCompSeasonPhase extends MatchList {
    private CompSeasonPhase compSeasonPhase;

    protected abstract String getParentPortalLink();

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        int pid = getIntValuedParameterValue(req, "pid");
        CompSeasonPhaseKey compSeasonPhaseKey = new CompSeasonPhaseKey(compSeasonKey, pid);

        compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(compSeasonPhaseKey);
    }

    @Override
    void writeMatchRow(Statement stat, H2HMatch h2HMatch, Map<Integer, ? extends Participant> particMap, Writer w) throws SQLException, IOException {
        if (compSeasonPhase.isHasParentMatches())
            writeMatchRowWithParentPortalLink(stat, h2HMatch, particMap, w);
        else
            writeMatchRowWithLinks(stat, h2HMatch, particMap, w);
    }

    @Override
    List<? extends H2HMatch> getMatchList(Statement stat, HttpServletRequest req,
                                          CompSeasonParticipantFactory<
                                                  ? extends CompSeasonParticipantKey,
                                                  ? extends CompSeasonPhaseParticipantKey,
                                                  ? extends Participant,
                                                  ? extends SuperKeyEntity,
                                                  ? extends H2HMatchKey,
                                                  ? extends H2HMatch,
                                                  ? extends H2HMatchPartKey,
                                                  ? extends H2HMatchPart,
                                                  ? extends H2HMatchPartStatKey,
                                                  ? extends H2HMatchPartStat> factory) throws SQLException {
        H2HMatchManager<? extends H2HMatchKey, ? extends H2HMatch> h2HMatchManager = factory.getH2HObjectFactory().getManager(stat);
        CompSeasonPhaseKey compSeasonPhaseKey = compSeasonPhase.getCompSeasonPhaseKey();

        return compSeasonPhase.isHasParentMatches() ?
                h2HMatchManager.getParentMatchesInCompSeasonPhase(compSeasonPhaseKey) :
                h2HMatchManager.getH2HMatchesFromCompSeasonPhases(Collections.singletonList(compSeasonPhaseKey));
    }

    private void writeMatchRowWithParentPortalLink(Statement stat,
                                                   H2HMatch h2HMatch,
                                                   Map<Integer, ? extends Participant> particMap,
                                                   Writer w) throws SQLException, IOException {
        String line = "<tr>" + getMatchParticipantInfo(h2HMatch, particMap) + "<td>";
        line += getScoreString(stat, h2HMatch);
        line += "</td><td>";
        line += getAnchor(h2HMatch, getManageMatchLink(), "ManageMatch", "md=u");
        line += "</td><td>";
        line += getAnchor(h2HMatch, getParentPortalLink(), "Manage Parent", null, "pmid");
        line += "</td></tr>";

        w.append(line);
    }
}
