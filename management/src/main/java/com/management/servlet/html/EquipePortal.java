package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EquipePortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("equipe");
        jsList.add("equipe");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<input id=\"nm\" type=\"text\" onkeypress=\"loadEquipeList();\" /><br/>\n");
        w.append("<table id=\"tblNm\" border=\"1\">\n</table>\n");
        w.append("<input type=\"button\" onclick=\"goToManageEquipe();\" value=\"Manage equipe\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToInsertEquipe();\" value=\"Insert equipe\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToEntityInstancePortal();\" value=\"Manage instances\" /><br/>\n");
    }
}
