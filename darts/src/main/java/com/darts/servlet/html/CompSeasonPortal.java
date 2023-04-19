package com.darts.servlet.html;

import com.sports.entity.Sport;
import jakarta.servlet.http.HttpServletRequest;

public class CompSeasonPortal extends com.sportservlet.html.CompSeasonPortal {
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected int getSportId(HttpServletRequest req) {
        return Sport.sportIdDarts;
    }
}
