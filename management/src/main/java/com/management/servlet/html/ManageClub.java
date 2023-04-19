package com.management.servlet.html;

import com.sports.entity.Club;
import com.sports.entity.manager.ClubManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageClub extends ManageEntity {
    private Club club;

    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "ClubPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("club");
        jsSpecificList.add("club");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return null;
    }

    @Override
    protected void preProcessSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        String nm = req.getParameter("nm");
        club = !"i".equals(mode) ? new ClubManager(stat).getClubByName(nm) : null;
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        if (club != null || "i".equals(mode)) {
            writeTextFieldWithLabel("Name", "nm", club != null ? club.getName() : null, w);
            writeSelectWithLabel("Geo", "geid",
                    getGeoMapWithCountries(stat), club != null ? club.getGeoId() : null, w);
        }
        else
            dispatchToReturnPath(req, res);
    }

    protected String getUpdateId(HttpServletRequest req) {
        return club != null ? "" + club.getId() : "";
    }
}
