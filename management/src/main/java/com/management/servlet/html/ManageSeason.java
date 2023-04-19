package com.management.servlet.html;

import com.sports.entity.Season;
import com.sports.entity.manager.SeasonManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageSeason extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "SeasonPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("season");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "sid";
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Season season = null;

        if ("u".equals(mode))
            season = new SeasonManager(stat).getSeason(seasonId);

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", season != null ? season.getName() : null, w);
        writeNumericTextField("Order", "o", season != null ? season.getOrder() : null, w);
    }
}
