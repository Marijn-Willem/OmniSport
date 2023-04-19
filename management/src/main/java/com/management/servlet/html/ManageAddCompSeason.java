package com.management.servlet.html;

import com.sportservlet.html.NonLinkedSeasonList;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageAddCompSeason extends NonLinkedSeasonList {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonManagementPortal?cid=" + competitionId;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseason");
        cssList.add("styling");
    }

    protected void initAbstractProperties() {

    }

    protected String getOnClick() {
        return "addCompSeasonLoader.loadElement();";
    }

    protected String getButtonValue() {
        return "Add Competition Season";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        w.append("cid = ");
        w.append(Integer.toString(competitionId));
        w.append(";\n");
    }
}
