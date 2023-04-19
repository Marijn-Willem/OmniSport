package com.sportservlet.ajax;

import com.sports.entity.Season;
import com.sports.entity.comparator.SeasonOrderDesc;
import com.sports.entity.manager.CompSeasonManager;
import com.sports.entity.manager.SeasonManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SeasonList extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Season> seasonList;
        SeasonManager sm = new SeasonManager(stat);

        if (competitionId != null) {
            List<Integer> seasonIds = new CompSeasonManager(stat).getSeasonIdsForCompetition(competitionId);
            seasonList = sm.getSeasonList(seasonIds);
        }
        else
            seasonList = sm.getAllSeasons();

        seasonList.sort(new SeasonOrderDesc());

        for (Season season : seasonList)
            ServletUtil.writeOption(season.getId(), season.getName(), resp.getWriter());
    }
}
