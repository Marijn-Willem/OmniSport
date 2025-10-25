package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public abstract class MatchesParentMatch extends MatchList {
    @Override
    List<? extends H2HMatch> getMatchList(Statement stat,
                                          HttpServletRequest req,
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
        return factory.getH2HObjectFactory().getMatchesForParent(stat, compSeasonKey, getIntValuedParameterValue(req, "pmid"));
    }

    @Override
    void writeMatchRow(Statement stat, H2HMatch h2HMatch, Map<Integer, ? extends Participant> particMap, Writer w) throws SQLException, IOException {
        writeMatchRowWithLinks(stat, h2HMatch, particMap, w);
    }
}
