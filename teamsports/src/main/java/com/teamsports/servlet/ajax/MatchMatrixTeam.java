package com.teamsports.servlet.ajax;

import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.CompDivision;
import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.Team;
import com.sports.entity.comparator.CompDivisionParentSort;
import com.sports.entity.comparator.TeamCompDivisionParentSortName;
import com.sports.logic.util.Util;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;

public class MatchMatrixTeam extends com.sportservlet.ajax.MatchMatrixTeam {
    private List<CompDivision> compDivisions;

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        super.init(stat, req);
        compDivisions = new DbCalculation(stat).getSortedCompDivisions(compSeasonKey);
        new com.sports.logic.calculation.DbCalculation(stat)
                .addCompDivisionsToTeams(participants, compSeasonKey);

        participants.forEach(this::setSortOrderData);
    }

    @Override
    protected void sortParticipants() {
        if (!compDivisions.isEmpty())
            participants.sort(new TeamCompDivisionParentSortName());
        else
            super.sortParticipants();
    }

    @Override
    protected void prepareData(List<List<List<H2HMatch>>> matchMatrix) {
        TeamCompDivisionAction teamCompDivisionAction = (team, compDivision) -> {
            compDivision.increaseNrTeams();
            if (compDivision.getFirstTeamId() == null)
                compDivision.setFirstTeamId(team.getId());
        };

        for (int i = 0; i < participants.size(); i++)
            participants.get(i).setSortIndex(i);

        copyCompDivisionFieldsToTeams();
        applyActionOnSortedTeams(teamCompDivisionAction);
        setRowSpansOnCompDivisions(matchMatrix);
        copyCompDivisionFieldsToTeams();
    }

    @Override
    protected String getOnClick(int mId) {
        return " onclick=\"goToMatchTimeLine(" + cspk.getCompetitionId() + ", " + cspk.getSeasonId() +
                ", " + cspk.getCompSeasonPhaseId() + ", " + mId + ");\"";
    }

    @Override
    protected void writeColumnHeaders(Writer w) throws IOException {
        if (!compDivisions.isEmpty())
            writeColumnHeadersWithCompDivisions(w);
        else
            super.writeColumnHeaders(w);
    }

    @Override
    protected void writeRowHeader(Participant partic, int rowSpan, Writer w) throws IOException {
        Team team = (Team) partic;
        if (team.getCompDivision() != null && team.getId() == team.getCompDivision().getFirstTeamId()) {
            CompDivision compDivision = team.getCompDivision();

            w.append("<th class=\"verticalHeader\" did=\"");
            w.append(Integer.toString(compDivision.getCompDivisionId()));
            w.append("\" rowspan=\"");
            w.append(Integer.toString(compDivision.getRowSpanMatchMatrix()));
            w.append("\">");
            w.append(Util.convertNullStringToEmpty(compDivision.getName()));
            w.append("</th>");
        }

        super.writeRowHeader(partic, rowSpan, w);
    }

    private void setRowSpansOnCompDivisions(List<List<List<H2HMatch>>> matchMatrix) {
        TeamCompDivisionAction teamCompDivisionAction = (team, compDivision) -> {
            List<List<H2HMatch>> matchesTeam = matchMatrix.get(team.getSortIndex());
            int rowSpan = 0;

            for (List<H2HMatch> matches : matchesTeam)
                rowSpan = Math.max(rowSpan, matches.size());

            compDivision.increaseRowSpanMatchMatrix(rowSpan);
        };

        applyActionOnSortedTeams(teamCompDivisionAction);
    }

    private void copyCompDivisionFieldsToTeams() {
        TeamCompDivisionAction teamCompDivisionAction = (team, compDivision) -> {
          team.getCompDivision().setParentDivisionSort(compDivision.getParentDivisionSort());
          team.getCompDivision().setFirstTeamId(compDivision.getFirstTeamId());
          team.getCompDivision().setRowSpanMatchMatrix(compDivision.getRowSpanMatchMatrix());
        };

        applyActionOnSortedTeams(teamCompDivisionAction);
    }

    private void setSortOrderData(Team team) {
        if (team.getCompDivision() != null) {
            CompDivision compDivisionForTeam = null;
            for (CompDivision compDivision : compDivisions)
                if (compDivision.getCompDivisionId() == team.getCompDivision().getCompDivisionId()) {
                    compDivisionForTeam = compDivision;
                    break;
                }

            if (compDivisionForTeam != null)
                team.getCompDivision().setParentDivisionSort(compDivisionForTeam.getParentDivisionSort());
        }
    }

    private void writeColumnHeadersWithCompDivisions(Writer w) throws IOException {
        w.append("<tr><th colspan=\"2\" rowspan=\"2\">");

        for (CompDivision compDivision : compDivisions) {
            if (compDivision.getNrTeams() != 0) {
                w.append("<th did=\"");
                w.append(Integer.toString(compDivision.getCompDivisionId()));
                w.append("\" colspan=\"");
                w.append(Integer.toString(compDivision.getNrTeams()));
                w.append("\">");
                w.append(Util.convertNullStringToEmpty(compDivision.getName()));
                w.append("</th>");
            }
        }

        w.append("</tr>\n<tr>");

        for (int i = 0; i < participants.size(); i++)
            writeColumnHeader(participants.get(i), i, w);

        w.append("</tr>\n");
    }

    private void applyActionOnSortedTeams(TeamCompDivisionAction teamCompDivisionAction) {
        Comparator<CompDivision> comparator = new CompDivisionParentSort();

        int teamIndX = 0;
        int compDivIndX = 0;

        while (teamIndX < participants.size() && compDivIndX < compDivisions.size()) {
            Team team = participants.get(teamIndX);
            CompDivision compDivision = compDivisions.get(compDivIndX);

            if (team.getCompDivision() == null || comparator.compare(team.getCompDivision(), compDivision) < 0)
                teamIndX++;
            else if (comparator.compare(compDivision, team.getCompDivision()) < 0)
                compDivIndX++;
            else {
                teamCompDivisionAction.applyAction(team, compDivision);
                teamIndX++;
            }
        }
    }

    private interface TeamCompDivisionAction {
        void applyAction(Team team, CompDivision compDivision);
    }
}
