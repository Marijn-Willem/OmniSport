package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EquipePortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<input id=\"eqn\" type=\"text\" onkeypress=\"loadEquipeNameList();\" />\n<br/>\n");
        w.append("<table id=\"tbl_eqn\">\n</table><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToPrepareEquipe();\" value=\"Manage Aliases\" /><br/>\n");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }
}
