package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class PersonPortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<input id=\"inp_ps\" type=\"text\" onkeypress=\"loadPersonSearchList();\" /><br/>\n");
        w.append("<table id=\"tbl_ps\"></table><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToPreparePerson();\" value=\"Manage Aliases\" /><br/>\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }
}
