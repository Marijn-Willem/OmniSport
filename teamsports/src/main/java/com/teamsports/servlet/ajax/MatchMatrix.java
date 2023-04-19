package com.teamsports.servlet.ajax;

import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.Participant;
import com.sports.entity.Team;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.CompSeasonTeamManager;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchMatrix extends com.sportservlet.ajax.MatchMatrix {
    private final Map<Integer, Integer> compDivisionMap = new HashMap<>();

    @Override
    protected void preProcess(Statement stat) throws SQLException {
        (new CompSeasonTeamManager(stat).getTeamsInCompSeason(compSeasonKey))
                .forEach(x -> compDivisionMap.put(x.getTeamId(), x.getCompDivisionId()));
    }

    @Override
    protected <S extends Participant> void sortParticipants(Statement stat, List<S> participants) throws SQLException {
        CompSeasonPhase compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(cspk);
        if (compSeasonPhase.isHasDivisionStandings())
            new DbCalculation(stat).sortTeamsOnCompDivision(cspk.getSuperKey(), (List<Team>) participants);
        else
            super.sortParticipants(stat, participants);
    }

    @Override
    protected String getOnClick(int mId) {
        return " onclick=\"goToMatchTimeLine(" + cspk.getCompetitionId() + ", " + cspk.getSeasonId() +
                ", " + mId + ");\"";
    }

    @Override
    protected void writeColumnHeader(Participant partic, int colIndX, Writer w) throws IOException {
        w.append("<th");
        writeConditionalDid(partic.getId(), w);
        w.append(" colindx=\"");
        w.append(Integer.toString(colIndX));
        w.append("\">");
        w.append(partic.getDescription());
        w.append("</th>");
    }

    @Override
    protected void writeRowHeader(Participant partic, int rowSpan, Writer w) throws IOException {
        w.append("<th rowspan=\"");
        w.append(Integer.toString(rowSpan));
        w.append("\"");
        writeConditionalDid(partic.getId(), w);
        w.append(">");
        w.append(partic.getDescription());
        w.append("</th>");
    }

    private void writeConditionalDid(int particId, Writer w) throws IOException {
        Integer did = compDivisionMap.get(particId);
        if (did != null) {
            w.append(" did=\"");
            w.append(did.toString());
            w.append("\"");
        }
    }
}
