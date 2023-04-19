package com.teamsports.servlet.html;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.TeamManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class ManageCompSeasonPhaseTeams extends SuperHtmlServlet {
    void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonphaseteam");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonPhaseVarsInScriptTag(req, w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPhasePortal?" + compSeasonUrlParameters;
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        List<CompSeasonTeam> compSeasonTeams = new CompSeasonTeamManager(stat).getTeamsInCompSeason(compSeasonKey);
        List<Team> teamList = new TeamManager(stat).getTeamList(compSeasonTeams.stream().map(CompSeasonTeam::getTeamId)
                .collect(Collectors.toList()));
        teamList.sort(new DescribedEntityDescription());

        Writer w = res.getWriter();

        w.append("<input class=\"cb\" id=\"cbAll\" type=\"checkbox\" onclick=\"handleClickCbAll();\" />");
        w.append("Select all<br/>\n<br/>\n");

        for (Team team : teamList) {
            w.append("<input class=\"cb\" type=\"checkbox\" name=\"tid\" value=\"");
            w.append(Integer.toString(team.getId()));
            w.append("\" />");
            w.append(team.getDescription());
            w.append("<br/>\n");
        }

        w.append("<br/>\n");
        w.append("<input id=\"btnSend\" type=\"button\" onclick=\"handleSend();\" value=\"Send\" /><br/>\n");
        w.append("<div id=\"divUpd\"></div>\n");
    }
}
