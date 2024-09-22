package com.teamsports.servlet.util;

import com.sports.calc.teamsports.Calculation;
import com.sports.entity.Team;
import com.sports.logic.util.Util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ServletUtil {
    public static void writeStandingHeader(Writer w, boolean bonusPoints) throws IOException {
        String line = "<tr><th></th><th>Name</th><th>Played</th><th>Points</th>" +
                (bonusPoints ? "<th>Bonus Points</th>" : "") +
                "<th>Diff</th></tr>\n";

        w.append(line);
    }

    public static void writeStandingUSAHeader(Writer w) throws IOException {
        w.append("<tr><th></th><th>Name</th><th>Played</th>");
        w.append("<th>Average</th><th>Av. Conference</th><th>Av. Division</th>");
        w.append("<th>Streak</th><th>Games behind</th></tr>\n");
    }

    public static void writeStanding(Writer w, List<Team> standing, boolean bonusPoints, boolean insertMatches)
        throws IOException {
        for (Team team : standing) {
            String trTag = "<tr" + (insertMatches ? " onclick=\"insertMatches(" + team.getId() + ");\"" : "") + ">";

            String row = trTag + "<td>" + team.getRank() + ".</td><td>" +
                    getTeamName(team) + "</td><td>" + team.getPlayed() + "</td><td>" + team.getPoints() +
                    (bonusPoints ? "</td><td>" + team.getBonusPoints() : "") +
                    "</td><td>" + team.getScoreDiff() + "</td></tr>\n";

            w.append(row);
        }
    }

    public static void writeStandingUSA(Writer w, List<Team> standing) throws IOException {
        for (Team team : standing) {
            w.append("<tr onclick=\"insertMatches(");
            w.append(Integer.toString(team.getId()));
            w.append(");\">");
            w.append("<td>");
            w.append(Integer.toString(team.getRank()));
            w.append(".</td><td>");
            w.append(getTeamName(team));
            w.append("</td><td>");
            w.append(Integer.toString(team.getPlayed()));
            w.append("</td><td>");
            w.append(Util.getDoubleAsStringWith3Digits(team.getAverage()));
            w.append("</td><td>");
            w.append(Util.getDoubleAsStringWith3Digits(team.getParentDivisionAverage()));
            w.append("</td><td>");
            w.append(Util.getDoubleAsStringWith3Digits(team.getDivisionAverage()));
            w.append("</td><td>");
            w.append(Calculation.getStreakAsString(team.getStreak()));
            w.append("</td><td>");
            w.append(Util.getDoubleAsStringWith2Digits(team.getGamesBehind()));
            w.append("</td></tr>\n");
        }
    }

    public static void writeStandingElements(Writer w) throws IOException {
        String rule = "<br/><input id=\"btnMatchMat\" type=\"button\" value=\"Match matrix\" " +
                "onclick=\"matchMatrixLoader.loadElement();\" />\n";
        w.append(rule);

        rule = "<br/><input id=\"btnMatchMatDiv\" type=\"button\" value=\"Match matrix divisions\" " +
                "onclick=\"goToMatchMatrixDivision();\" />\n";
        w.append(rule);

        w.append("<br/><table id=\"tblStanding\" border=\"1\">\n</table>\n");
        w.append("<br/><table id=\"tblMatches\" border=\"1\">\n</table>\n");
        w.append("<br/><table id=\"tblMatchMatrix\" border=\"1\">\n</table>\n");
    }

    public static String getTeamName(Team team) {
        String compDivisionName = team.getCompDivision() != null ? team.getCompDivision().getName() : null;

        return team.getDescription() + Util.getPrefixedStringOrEmptyString(
                Util.getStringBetweenBracketsOrEmptyString(compDivisionName), " ");
    }
}
