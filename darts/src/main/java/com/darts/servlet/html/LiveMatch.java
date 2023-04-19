package com.darts.servlet.html;

import com.sports.entity.PersonMatch;
import com.sports.entity.key.PersonMatchKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class LiveMatch extends SuperHtmlServlet {
    protected void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("livematch");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        PersonMatchKey personMatchKey = (PersonMatchKey)req.getSession().getAttribute("pmk");
        return "MatchOverview?cid=" + personMatchKey.getCompetitionId() + "&sid=" + personMatchKey.getSeasonId();
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        PersonMatch personMatch = (PersonMatch)req.getSession().getAttribute("pm");

        Writer w = res.getWriter();
        String rule = "<form action=\"" + path + "/ProcessLeg\">\n";
        w.append(rule);

        w.append("<table>\n");
        rule = "<tr>" + getScoreCurTd("1") + getScoreCurTd("2") + "</tr>\n";
        w.append(rule);

        rule = "<tr><td>" + personMatch.getPerson1Name() + "</td><td>" + personMatch.getPerson2Name() + "</td></tr>\n";
        w.append(rule);

        rule = "<tr><td>" + getScoreInput("1") + "</td><td>" + getScoreInput("2") + "</td></tr>\n";
        w.append(rule);

        rule = "<tr><td>" + getScoreButton("1") + "</td><td>" + getScoreButton("2") + "</td></tr>\n";
        w.append(rule);

        rule = "<tr><td>" + getDartSelect("1") + "</td><td>" + getDartSelect("2") + "</td></tr>\n";
        w.append(rule);

        rule = "<tr><td>" + getMisDubControls("1") + "</td><td>" + getMisDubControls("2") + "</td></tr>\n";
        w.append(rule);

        w.append("</table>\n");
        w.append("<input type=\"hidden\" id=\"p1sc\" name=\"p1sc\" />\n");
        w.append("<input type=\"hidden\" id=\"p2sc\" name=\"p2sc\" />\n");
        w.append("<input type=\"hidden\" id=\"p1w\" name=\"p1w\" />\n");

        w.append("</form>\n");
    }

    private String getScoreCurTd(String person) {
        return "<td id=\"sc_cur" + person + "\">501</td>";
    }

    private String getScoreInput(String person) {
        return "<input id=\"scr" + person + "\" type=\"text\" />";
    }

    private String getDartSelect(String person) {
        String dartSelect = "<select id=\"drt" + person + "\">\n";
        dartSelect += "<option value=\"1\">1</option>\n";
        dartSelect += "<option value=\"2\">2</option>\n";
        dartSelect += "<option value=\"3\" selected=\"selected\">3</option>\n";
        dartSelect += "</select>\n";

        return dartSelect;
    }

    private String getMisDubControls(String person) {
        String idName = "p" + person + "md";

        String mdCtrl = "<input type=\"button\" value=\"<\" onclick=\"addMisDub(" + person + ", false);\" />";
        mdCtrl += "<input type=\"text\" id=\"" + idName + "\" name=\"" + idName + "\" value=\"0\" />";
        mdCtrl += "<input type=\"button\" value=\">\" onclick=\"addMisDub(" + person + ", true);\" />";

        return mdCtrl;
    }

    private String getScoreButton(String person) {
        return "<input type=\"button\" value=\"Add score\" onclick=\"addScore(" + person + ");\" />";
    }
}
