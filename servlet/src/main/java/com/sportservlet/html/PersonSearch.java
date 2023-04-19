package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class PersonSearch extends SuperHtmlServlet implements AbstractHtmlServlet {
    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("person");
        jsList.add("name");

        initSpecificProperties(req);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<input type=\"text\" id=\"inp_pls\" name=\"nm\" " +
                "onkeypress=\"personSearchListLoader.loadElement();\"/><br/>\n");
        w.append("<table id=\"tbl_pls\" border=\"1\">\n</table>\n");
        w.append("<input type=\"button\" value=\"Add to selection\" onclick=\"addToSelection();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Count selection\" onclick=\"countSelection();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Add to memory\" onclick=\"addToMemory();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Dedouble persons\" onclick=\"dedoublePersons();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Split person\" onclick=\"splitPerson();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Manage person\" onclick=\"goToManagePerson();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Person instances\" onclick=\"goToEntityInstancePortal();\" /><br/>\n");
        w.append("<table id=\"tbl_sel\" border=\"1\">\n</table>\n");
        w.append("<div id=\"div_mem\"></div>\n");
    }
}
