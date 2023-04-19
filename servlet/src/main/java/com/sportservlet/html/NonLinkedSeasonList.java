package com.sportservlet.html;

import com.sports.entity.Season;
import com.sports.entity.comparator.SeasonId;
import com.sports.entity.comparator.SeasonOrderDesc;
import com.sports.entity.manager.CompSeasonManager;
import com.sports.entity.manager.SeasonManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class NonLinkedSeasonList extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected abstract void initAbstractProperties();
    protected abstract String getOnClick();
    protected abstract String getButtonValue();

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        initAbstractProperties();
        initSpecificProperties(req);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        List<Season> seasonList = new SeasonManager(stat).getAllSeasons();
        List<Integer> seasonIds = new CompSeasonManager(stat).getSeasonIdsForCompetition(competitionId);

        Comparator<Season> comparator = new SeasonId();
        seasonList.sort(comparator);

        for (int sId : seasonIds) {
            Season seasonSearch = new Season();
            seasonSearch.setId(sId);

            seasonList.remove(Collections.binarySearch(seasonList, seasonSearch, comparator));
        }

        seasonList.sort(new SeasonOrderDesc());

        Writer w = res.getWriter();
        w.append("<div>\n<select id=\"selSid\">\n");

        String line;

        for (Season season : seasonList) {
            line = "<option value=\"" + season.getId() + "\">" + season.getName() + "</option>\n";
            w.append(line);
        }

        w.append("</select>\n</div>\n");

        line = "<input type=\"button\" onclick=\"" + getOnClick() + "\" " +
                "value=\"" + getButtonValue() + "\" />\n<div id=\"divResp\"></div>\n";

        w.append(line);
    }
}
